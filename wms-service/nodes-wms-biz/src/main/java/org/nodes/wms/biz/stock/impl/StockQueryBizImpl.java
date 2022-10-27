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
 * 库存查询相关业务
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
		AssertUtil.notNull(stage, "根据箱码查询入库暂存区库存失败，没有查到对应的入库暂存区");
		return stockDao.getStockByBoxCode(boxCode, Collections.singletonList(stage.get(0).getLocId()));
	}

	@Override
	public List<CallAgvResponse> findLpnStockOnStageLeftByCallAgv(Long whId, String boxCode) {
		// 创建返回集合
		List<CallAgvResponse> callAgvResponseList = new ArrayList<>();
		// 根据仓库id获取入库暂存区库位
		List<Location> stage = locationBiz.getLocationByZoneType(whId, DictKVConstant.ZONE_TYPE_STAGE);
		if (Func.isEmpty(stage)) {
			throw new ServiceException("查询失败,该库房没有配置入库暂存区");
		}
		// 根据箱码和库房id获取入库暂存区库存
		List<Stock> stockList = findLpnStockOnStageLeft(whId, boxCode, stage.get(0));
		// 按lpn编码对查询出的集合进行分组
		Map<String, List<Stock>> stockMap = stockList.stream().collect(Collectors.groupingBy(Stock::getLpnCode));
		// 遍历分组后的map
		for (Map.Entry<String, List<Stock>> entry : stockMap.entrySet()) {
			// 取出每个分组里的库存集合
			List<Stock> stocks = entry.getValue();
			// 获取箱型
			String lpnType = lpnTypeBiz.tryParseBoxCode(stocks.get(0).getBoxCode()).getCode();
			// 如果是D箱型则根据lpn查询同一托盘的所有库存
			if (lpnType.equals(LpnTypeCodeEnum.D.getCode())) {
				// 根据lpnCoe获取同一托盘的所有库存
				stocks = stockDao.getStockByLpnCode(stocks.get(0).getLpnCode(),
					Collections.singletonList(stage.get(0).getLocId()));
			}
			// 创建返回对象并添加到集合中
			CallAgvResponse callAgvResponse = createCallAgvResponse(stocks, lpnType);
			callAgvResponseList.add(callAgvResponse);
		}
		return callAgvResponseList;
	}

	@Override
	public Stock findStockOnStage(ReceiveLog receiveLog) {
		return stockMergeStrategy.matchSameStock(receiveLog);
	}

	@Override
	public Stock findStockOnPickTo(LogSoPick pickLog) {
		List<Location> pickToLoc = locationBiz.getLocationByZoneType(pickLog.getWhId(),
			DictKVConstant.ZONE_TYPE_PICK_TO);
		return stockMergeStrategy.matchSameStock(pickLog, pickToLoc.get(0));
	}

    @Override
    public List<Stock> findStockOnPickTo(String boxCode) {
		AssertUtil.notEmpty(boxCode, "按箱码查询出库暂存区的库存失败,箱码不能为空");

		List<Location> allPickToLocation = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_PICK_TO);
		AssertUtil.notEmpty(allPickToLocation, "按箱码查询出库暂存区的库存失败,没有获取到出库暂存区的库位信息");

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
		if (Func.isNotEmpty(pickToLocs)){
			pickToLocIds = pickToLocs.stream().map(Location::getLocId).collect(Collectors.toList());
		}

		return stockDao.getStockByLpnCodeExcludeLoc(lpnCode, pickToLocIds);
    }

    @Override
	public StockIndexResponse staticsStockDataOnIndexPage() {
		// 获取所有入库暂存区库位
		List<Location> allStageList = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_STAGE);
		// 获取所有入库检验区库位
		List<Location> allQcList = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_INSTOCK_QC);
		// 获取所有出库暂存区库位
		List<Location> allPickToList = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_PICK_TO);
		// 根据入库暂存区id获取入库暂存区的物品数量和存放天数
		Map<String, Object> stageStock = stockDao.getStockQtyByLocIdList(
			allStageList.stream().map(Location::getLocId).collect(Collectors.toList()));
		// 根据入库检验区id获取入库检验区的物品数量和存放天数
		Map<String, Object> qcStock = stockDao.getStockQtyByLocIdList(
			allQcList.stream().map(Location::getLocId).collect(Collectors.toList()));
		// 根据出库暂存区id获取库存中不是入库暂存区的物品总数
		int stockSkuCount = stockDao.getStockSkuCountByLocIdList(
			allPickToList.stream().map(Location::getLocId).collect(Collectors.toList()));
		// 查询库位总数
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
			// 保留三位小数
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
		AssertUtil.notNull(locationId, "库存查询失败，库位不能为空");

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
//						throw new ServiceException("查询库存余额时不能选择出库集货区");
//					}
//				}
//			}
//		}

		// 按箱显示或按lpn显示
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
//			// 设置库存可用量
//			stockPageResponse.setStockEnable(stockPageResponse.getStockQty()
//				.add(stockPageResponse.getPickQty().subtract(stockPageResponse.getOccupyQty())));
//			// 设置库存余额
//			stockPageResponse.setStockBalance(stockPageResponse.getStockQty().subtract(stockPageResponse.getPickQty()));
		}
		ExcelUtil.export(response, "库存统计", "库存统计数据表", stockListCount, StockPageResponse.class);

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

	private List<Stock> findLpnStockOnStageLeft(Long whId, String boxCode, Location stage) {
		// 根据箱码和库位查询入库暂存区的库存
		List<Stock> stockList = stockDao.getStockLeftLikeByBoxCode(boxCode,
			Collections.singletonList(stage.getLocId()));
		if (Func.isEmpty(stockList)) {
			throw new ServiceException("没有查询到相关库存信息");
		}
		for (Stock stock : stockList) {
			if (Func.isEmpty(stock.getLpnCode())) {
				throw new ServiceException("查询失败,lpn编码为空");
			}
		}
		return stockList;

	}

	private CallAgvResponse createCallAgvResponse(List<Stock> stockList, String lpnType) {
		// 创建返回对象
		CallAgvResponse callAgvResponse = new CallAgvResponse();
		callAgvResponse.setLpnType(lpnType);
		callAgvResponse.setLpnCode(stockList.get(0).getLpnCode());
		// 初始化总数量
		BigDecimal qty = new BigDecimal(0);
		// 初始化箱码对象集合
		List<BoxDto> boxDtoList = new ArrayList<>();
		// 根据箱码对集合进行分组
		Map<String, List<Stock>> stockMap = stockList.stream().collect(Collectors.groupingBy(Stock::getBoxCode));
		for (Map.Entry<String, List<Stock>> entry : stockMap.entrySet()) {
			// 取出每个分组里的库存集合
			List<Stock> stocks = entry.getValue();
			// 创建箱码对象
			BoxDto boxDto = new BoxDto();
			// 初始化库存id集合
			List<Long> stockIdList = new ArrayList<>();
			// 初始化箱码对象数量
			BigDecimal boxQty = new BigDecimal(0);
			// 遍历集合
			for (Stock stock : stocks) {
				// 数量进行累加
				boxQty = boxQty.add(StockUtil.getStockBalance(stock));
				// 添加库存id到集合中
				stockIdList.add(stock.getStockId());
			}
			// 设置box对象箱码
			boxDto.setBoxCode(stocks.get(0).getBoxCode());
			// 设置box对象数量
			boxDto.setQty(boxQty);
			// 设置box对象库存id集合
			boxDto.setStockIdList(stockIdList);
			// 总数量进行累加
			qty = qty.add(boxQty);
			// 箱码对象添加到箱码集合中
			boxDtoList.add(boxDto);
		}
		// 设置总数量
		callAgvResponse.setQty(qty);
		// 设置箱码对象集合
		callAgvResponse.setBoxList(boxDtoList);
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
		return stockDao.getStockByDropId(dropId);
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
