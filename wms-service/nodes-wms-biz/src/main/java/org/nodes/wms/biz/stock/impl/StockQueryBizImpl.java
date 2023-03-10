package org.nodes.wms.biz.stock.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.DictKVConstant;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.stock.merge.StockMergeStrategy;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeCodeEnum;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.common.stock.StockUtil;
import org.nodes.wms.dao.count.dto.output.PdaBoxQtyResponse;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.putaway.dto.output.BoxDto;
import org.nodes.wms.dao.putaway.dto.output.CallAgvResponse;
import org.nodes.wms.dao.stock.SerialDao;
import org.nodes.wms.dao.stock.StockDao;
import org.nodes.wms.dao.stock.StockLogDao;
import org.nodes.wms.dao.stock.dto.input.FindAllStockByNoRequest;
import org.nodes.wms.dao.stock.dto.input.StockBySerialPageQuery;
import org.nodes.wms.dao.stock.dto.input.StockLogPageQuery;
import org.nodes.wms.dao.stock.dto.input.StockPageQuery;
import org.nodes.wms.dao.stock.dto.output.*;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.ConvertUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ????????????????????????
 *
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
public class StockQueryBizImpl implements StockQueryBiz {

	private final ZoneBiz zoneBiz;
	private final StockDao stockDao;
	private final LocationBiz locationBiz;
	private final LpnTypeBiz lpnTypeBiz;
	private final StockLogDao stockLogDao;
	private final SerialDao serialDao;
	private final StockMergeStrategy stockMergeStrategy;

	@Override
	public List<Serial> findSerialBySerialNo(List<String> serialNoList) {
		return serialDao.getSerialBySerialNo(serialNoList);
	}

	@Override
	public List<Serial> findSerialByStock(Long stockId) {
		return serialDao.getSerialByStockId(stockId);
	}

	@Override
	public List<Stock> findEnableStockByBoxCode(String boxCode) {
		List<Long> pickToLocList = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_PICK_TO)
			.stream()
			.map(Location::getLocId)
			.collect(Collectors.toList());
		return stockDao.getStockByBoxCodeExcludeLoc(Collections.singletonList(boxCode), pickToLocList);
	}

	@Override
	public List<Stock> findEnableStockByBoxCode(List<String> boxCodeList) {
		List<Long> pickToLocList = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_PICK_TO)
			.stream()
			.map(Location::getLocId)
			.collect(Collectors.toList());
		return stockDao.getStockByBoxCodeExcludeLoc(boxCodeList, pickToLocList);
	}

	@Override
	public List<Stock> findStockOnStageByBoxCode(Long whId, String boxCode) {
		List<Location> stage = locationBiz.getLocationByZoneType(whId, DictKVConstant.ZONE_TYPE_STAGE);
		AssertUtil.notNull(stage, "????????????????????????????????????????????????????????????????????????????????????");
		return stockDao.getStockByBoxCode(boxCode, Collections.singletonList(stage.get(0).getLocId()));
	}

	@Override
	public List<CallAgvResponse> findLpnStockOnStageLeftByCallAgv(Long whId, String boxCode) {
		// ??????????????????
		List<CallAgvResponse> callAgvResponseList = new ArrayList<>();
		// ??????????????????????????????
		List<Stock> stockList = findStockByCallAgv(whId, boxCode);
		if (Func.isEmpty(stockList)) {
			throw new ServiceException("???????????????????????????????????????????????????????????????????????????????????????0");
		}
		// ???lpn???????????????????????????????????????
		Map<String, List<Stock>> stockMap = stockList.stream().collect(Collectors.groupingBy(Stock::getLpnCode));
		// ??????????????????map
		for (Map.Entry<String, List<Stock>> entry : stockMap.entrySet()) {
			// ????????????????????????????????????
			List<Stock> stocks = entry.getValue();
			// ????????????
			String lpnType = lpnTypeBiz.tryParseBoxCode(stocks.get(0).getBoxCode()).getCode();
			// ?????????D???????????????lpn?????????????????????????????????
			if (lpnType.equals(LpnTypeCodeEnum.D.getCode())) {
				// ??????lpnCoe?????????????????????????????????
				stocks = stockDao.getStockByLpnCode(stocks.get(0).getLpnCode(),
					Collections.singletonList(stocks.get(0).getLocId()));
			}
			// ???????????????????????????????????????
			CallAgvResponse callAgvResponse = createCallAgvResponse(stocks, lpnType);
			callAgvResponseList.add(callAgvResponse);
		}
		return callAgvResponseList;
	}

	private List<Stock> findStockByCallAgv(Long whId, String boxCode) {
		return stockDao.getStockByZoneTypeList(whId, boxCode);
	}


	@Override
	public Stock findStockOnStage(ReceiveLog receiveLog) {
		return stockMergeStrategy.matchSameStock(receiveLog);
	}

	@Override
	public Stock findStockOnPickTo(LogSoPick pickLog) {
		List<Location> pickToLoc = locationBiz.getLocationByZoneType(pickLog.getWhId(),
			DictKVConstant.ZONE_TYPE_PICK_TO);
		return stockMergeStrategy.matchSameStock(pickLog, pickToLoc.get(0), false);
	}

	@Override
	public List<Stock> findStockOnPickTo(String boxCode) {
		AssertUtil.notEmpty(boxCode, "?????????????????????????????????????????????,??????????????????");

		List<Location> allPickToLocation = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_PICK_TO);
		AssertUtil.notEmpty(allPickToLocation, "?????????????????????????????????????????????,?????????????????????????????????????????????");

		List<Long> allPickToLocIds = allPickToLocation.stream()
			.map(Location::getLocId)
			.collect(Collectors.toList());
		return stockDao.getStockByBoxCode(boxCode, allPickToLocIds);
	}

	@Override
	public List<Stock> findStockByLpnCode(String lpnCode) {
		return stockDao.getStockByLpnCode(lpnCode, null);
	}

	@Override
	public List<Stock> findStockByLpnCodeOnStore(String lpnCode) {
		List<Location> pickToLocs = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_PICK_TO);
		List<Long> pickToLocIds = null;
		if (Func.isNotEmpty(pickToLocs)) {
			pickToLocIds = pickToLocs.stream().map(Location::getLocId).collect(Collectors.toList());
		}

		return stockDao.getStockByLpnCodeExcludeLoc(lpnCode, pickToLocIds);
	}

	@Override
	public StockIndexResponse staticsStockDataOnIndexPage() {
		// ?????????????????????????????????
		List<Location> allStageList = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_STAGE);
		// ?????????????????????????????????
		List<Location> allQcList = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_INSTOCK_QC);
		// ?????????????????????????????????
		List<Location> allPickToList = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_PICK_TO);
		// ?????????????????????id???????????????????????????????????????????????????
		Map<String, Object> stageStock = stockDao.getStockQtyByLocIdList(
			allStageList.stream().map(Location::getLocId).collect(Collectors.toList()));
		// ?????????????????????id???????????????????????????????????????????????????
		Map<String, Object> qcStock = stockDao.getStockQtyByLocIdList(
			allQcList.stream().map(Location::getLocId).collect(Collectors.toList()));
		// ?????????????????????id???????????????????????????????????????????????????
		int stockSkuCount = stockDao.getStockSkuCountByLocIdList(
			allPickToList.stream().map(Location::getLocId).collect(Collectors.toList()));
		// ??????????????????
		int locCount = locationBiz.countAll();
		StockIndexResponse response = new StockIndexResponse();

		if (Func.isNotEmpty(stageStock)) {
			Optional<BigDecimal> stageQty = Optional
				.ofNullable(ConvertUtil.convert(stageStock.get("skuQty"), BigDecimal.class));
			response.setStageSkuQty(stageQty.orElse(BigDecimal.ZERO).setScale(3, RoundingMode.DOWN));
			response.setStageSkuStoreDay(ConvertUtil.convert(stageStock.get("skuStoreDay"), Integer.class));
		} else {
			response.setStageSkuQty(BigDecimal.ZERO);
			response.setStageSkuStoreDay(0);
		}

		if (Func.isNotEmpty(qcStock)) {
			Optional<BigDecimal> qcQty = Optional
				.ofNullable(ConvertUtil.convert(qcStock.get("skuQty"), BigDecimal.class));
			// ??????????????????
			response.setQcSkuQty(qcQty.orElse(BigDecimal.ZERO).setScale(3, RoundingMode.DOWN));
			response.setQcSkuStoreDay(
				ConvertUtil.convert(qcStock.get("skuStoreDay"), Integer.class));
		} else {
			response.setQcSkuQty(BigDecimal.ZERO);
			response.setQcSkuStoreDay(0);
		}

		response.setStockSkuCount(stockSkuCount);
		if (stockSkuCount == 0) {
			response.setLocOccupy((double) 0);
		} else {
			BigDecimal decimal = new BigDecimal((double) stockSkuCount / locCount * 100);
			response.setLocOccupy(decimal.setScale(2, RoundingMode.HALF_UP).doubleValue());
		}
		return response;
	}

	@Override
	public Page<StockLogPageResponse> pageStockLog(Query query,
												   StockLogPageQuery stockLogPageQuery) {
		return stockLogDao.page(Condition.getPage(query), stockLogPageQuery);
	}

	@Override
	public List<Stock> findStockByLocation(List<Location> locationList) {
		List<Long> locIdList = locationList.stream()
			.map(Location::getLocId)
			.distinct()
			.collect(Collectors.toList());
		return stockDao.getStockByLocIdList(locIdList);
	}

	@Override
	public List<Stock> findStockByLocation(Long locationId) {
		AssertUtil.notNull(locationId, "???????????????????????????????????????");

		return stockDao.getStockByLocIdList(Collections.singletonList(locationId));
	}

	@Override
	public boolean isEmptyLocation(Long locationId) {
		List<Stock> stocks = findStockByLocation(locationId);
		return Func.isEmpty(stocks) || stocks.size() == 0;
	}

	@Override
	public List<Stock> findEnableStockByZoneTypeAndSkuLot(Long whId, Long skuId, StockStatusEnum stockStatusEnum,
														  List<String> zoneTypeList, SkuLotBaseEntity skuLot) {
		List<Long> zoneIdList = null;
		if (Func.isNotEmpty(zoneTypeList)) {
			List<Zone> zoneList = zoneBiz.findByZoneType(zoneTypeList);
			zoneIdList = zoneList.stream()
				.map(Zone::getZoneId)
				.collect(Collectors.toList());
		}

		return findEnableStockByZoneAndSkuLot(whId, skuId, stockStatusEnum, zoneIdList, skuLot);
	}

	@Override
	public List<Stock> findEnableStockByZoneAndSkuLot(Long whId, Long skuId, StockStatusEnum stockStatusEnum,
													  List<Long> zoneIdList, SkuLotBaseEntity skuLot) {
		Long pickToZoneId = locationBiz.getLocationByZoneType(whId, DictKVConstant.ZONE_TYPE_PICK_TO).get(0)
			.getZoneId();
		return stockDao.findEnableStockByZone(whId, skuId, stockStatusEnum,
			zoneIdList, skuLot, Collections.singletonList(pickToZoneId));
	}

	@Override
	public List<Stock> findEnableStockByLocationAndSkuLot(Long whId, Long skuId, StockStatusEnum stockStatusEnum,
														  List<Long> locationIdList, SkuLotBaseEntity skuLot) {
		Long pickToZoneId = locationBiz.getLocationByZoneType(whId, DictKVConstant.ZONE_TYPE_PICK_TO).get(0)
			.getZoneId();
		return stockDao.findEnableStockByLocation(whId, skuId, stockStatusEnum,
			locationIdList, skuLot, Collections.singletonList(pickToZoneId));
	}

	@Override
	public Stock findStockById(Long stockId) {
		return stockDao.getStockById(stockId);
	}

	@Override
	public IPage<FindAllStockByNoResponse> selectStockList(FindAllStockByNoRequest request, Query query) {
		IPage<Stock> page = Condition.getPage(query);
		return stockDao.getStockList(request, page);
	}

	@Override
	public Page<StockPageResponse> getStockPage(Query query, StockPageQuery stockPageQuery) {
//		if (Func.isNotEmpty(stockPageQuery.getZoneIdList())) {
//			for (Long zoneId : stockPageQuery.getZoneIdList()) {
//				List<Location> locationList = locationBiz.findLocationByZoneId(zoneId);
//				for (Location location : locationList) {
//					if (locationBiz.isPickToLocation(location)) {
//						throw new ServiceException("????????????????????????????????????????????????");
//					}
//				}
//			}
//		}

		// ??????????????????lpn??????
		if (Func.isNotEmpty(stockPageQuery.getIsShowByBox()) || Func.isNotEmpty(stockPageQuery.getIsShowByLpn())) {
			List<StockPageResponse> stockPageResponseList = stockDao.getStockResponseByBoxOrByLpn(stockPageQuery);
			Page<StockPageResponse> page = new Page<>();
			page.setRecords(stockPageResponseList);
			page.setTotal(stockPageResponseList.size());
			return page;
		}
		return stockDao.page(Condition.getPage(query), stockPageQuery);
	}

	@Override
	public void getStockListCount(StockPageQuery stockPageQuery, HttpServletResponse response) {
		List<StockPageResponse> stockListCount = stockDao.getStockListCount(stockPageQuery);
		for (StockPageResponse stockPageResponse : stockListCount) {
			String stockStatus = stockPageResponse.getStockStatus().getDesc();
			stockPageResponse.setStockStatusDesc(stockStatus);
			// ?????????????????????,??????
			List<String> serialList = serialDao.getSerialNoByStockId(stockPageResponse.getStockId());
			if (Func.isNotEmpty(serialList)) {
				stockPageResponse.setSnCode(String.join(",", serialList));
			}
//			// ?????????????????????
//			stockPageResponse.setStockEnable(stockPageResponse.getStockQty()
//				.add(stockPageResponse.getPickQty().subtract(stockPageResponse.getOccupyQty())));
//			// ??????????????????
//			stockPageResponse.setStockBalance(stockPageResponse.getStockQty().subtract(stockPageResponse.getPickQty()));
		}
		ExcelUtil.export(response, "????????????", "?????????????????????", stockListCount, StockPageResponse.class);

	}

	@Override
	public List<Stock> findStockById(List<Long> stockIdList) {
		return stockDao.getStockById(stockIdList);
	}

	@Override
	public IPage<StockBySerialPageResponse> getStockBySerialPage(Query query,
																 StockBySerialPageQuery stockBySerialPageQuery) {
		return stockDao.page(Condition.getPage(query), stockBySerialPageQuery);
	}

	@Override
	public List<PdaBoxQtyResponse> getStockCountByLocCode(String locCode, String boxCode, String skuCode) {
		return stockDao.getStockCountByLocCode(locCode, boxCode, skuCode);
	}

	@Override
	public int getSerialCountByStockId(Long stockId) {
		return serialDao.getSerialCountByStockId(stockId);
	}

	private List<Stock> findStockByLocIdList(Long whId, String boxCode, List<Long> locIdList) {
		// ???????????????????????????????????????????????????
		List<Stock> stockList = stockDao.getStockLeftLikeByBoxCode(boxCode, locIdList);

		if (Func.isEmpty(stockList)) {
			throw new ServiceException("?????????????????????????????????");
		}
		for (Stock stock : stockList) {
			if (Func.isEmpty(stock.getLpnCode())) {
				throw new ServiceException("????????????,lpn????????????");
			}
		}
		return stockList;

	}

	private CallAgvResponse createCallAgvResponse(List<Stock> stockList, String lpnType) {
		// ??????????????????
		CallAgvResponse callAgvResponse = new CallAgvResponse();
		callAgvResponse.setLpnType(lpnType);
		callAgvResponse.setLpnCode(stockList.get(0).getLpnCode());
		// ??????????????????
		BigDecimal qty = new BigDecimal(0);
		// ???????????????????????????
		List<BoxDto> boxDtoList = new ArrayList<>();
		// ?????????????????????????????????
		Map<String, List<Stock>> stockMap = stockList.stream().collect(Collectors.groupingBy(Stock::getBoxCode));
		for (Map.Entry<String, List<Stock>> entry : stockMap.entrySet()) {
			// ????????????????????????????????????
			List<Stock> stocks = entry.getValue();
			// ??????????????????
			BoxDto boxDto = new BoxDto();
			// ???????????????id??????
			List<Long> stockIdList = new ArrayList<>();
			// ???????????????????????????
			BigDecimal boxQty = new BigDecimal(0);
			// ????????????
			for (Stock stock : stocks) {
				// ??????????????????
				boxQty = boxQty.add(StockUtil.getStockBalance(stock));
				// ????????????id????????????
				stockIdList.add(stock.getStockId());
			}
			// ??????box????????????
			boxDto.setBoxCode(stocks.get(0).getBoxCode());
			// ??????box????????????
			boxDto.setQty(boxQty);
			// ??????box????????????id??????
			boxDto.setStockIdList(stockIdList);
			// ?????????????????????
			qty = qty.add(boxQty);
			// ????????????????????????????????????
			boxDtoList.add(boxDto);
		}
		// ???????????????
		callAgvResponse.setQty(qty);
		// ????????????????????????
		callAgvResponse.setBoxList(boxDtoList);
		//??????????????????
		callAgvResponse.setLocCode(stockList.get(0).getLocCode());
		return callAgvResponse;
	}

	@Override
	public List<Stock> findEnableStockByZoneTypeAndSkuLot(SkuLotBaseEntity skuLot) {
		List<Location> allPickToLocation = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_PICK_TO);
		List<Long> pickToLocIdList = allPickToLocation.stream()
			.map(Location::getLocId)
			.collect(Collectors.toList());
		return stockDao.getEnableStockBySkuLotAndExcludeLoc(pickToLocIdList, skuLot);
	}

	@Override
	public List<Stock> findStockByDropId(Long dropId) {
		return stockDao.getStockByDropId(dropId.toString());
	}

	@Override
	public List<Stock> findEnableStockBySkuAndSkuLot(Long skuId, SkuLotBaseEntity skuLot) {
		List<Location> pickToLocationList = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_PICK_TO);
		List<Long> pickToLocIdList = pickToLocationList.stream()
			.map(Location::getLocId)
			.collect(Collectors.toList());
		return stockDao.getEnableStockBySkuLotAndExcludeLoc(skuId, pickToLocIdList, skuLot);
	}

}
