package org.nodes.wms.biz.stockManage.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.DictKVConstant;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.putaway.strategy.TianYiPutawayStrategy;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.stockManage.StockManageBiz;
import org.nodes.wms.biz.task.AgvTask;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.lpntype.entities.LpnType;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.stock.dto.input.*;
import org.nodes.wms.dao.stock.dto.output.EstimateStockMoveResponse;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@Service
@RequiredArgsConstructor
public class StockManageBizImpl implements StockManageBiz {
	private final StockQueryBiz stockQueryBiz;
	private final StockBiz stockBiz;
	private final LocationBiz locationBiz;
	private final SkuBiz skuBiz;
	private final LogBiz logBiz;
	private final LpnTypeBiz lpnTypeBiz;
	private final TianYiPutawayStrategy tianYiPutawayStrategy;
	private final AgvTask agvTask;
	private final ZoneBiz zoneBiz;

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void freezeByLocCodeAction(String locCode, Long whId) {
		Location location = locationBiz.findLocationByLocCode(whId, locCode);
		AssertUtil.notNull(location, "????????????????????????????????????????????????????????????????????????");
		List<Long> locIds = new ArrayList<>();
		locIds.add(location.getLocId());
		stockBiz.freezeStockByLoc(locIds);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void freezeByLotNumberAction(String lotNumber) {
		AssertUtil.notEmpty(lotNumber, "????????????????????????????????????");
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		skuLot.setSkuLot1(lotNumber);
		List<Stock> stockList = stockQueryBiz.findEnableStockByZoneTypeAndSkuLot(skuLot);
		AssertUtil.notNull(stockList, "?????????????????????,???????????????????????????????????????");
		List<Long> stockIds = stockList.stream()
			.map(Stock::getStockId)
			.distinct()
			.collect(Collectors.toList());
		stockBiz.freezeStock(stockIds);
	}

	@Override
	public void freezeBySerialNumberAction(List<String> serialNumber) {

	}

	@Override
	public void portionFreezeAction(PortionFreezeRequest request) {

	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void freezeStockByBoxCodeAction(String boxCode) {
		AssertUtil.notNull(boxCode, "??????????????????????????????");
		List<String> boxCodes = new ArrayList<>();
		boxCodes.add(boxCode);
		stockBiz.freezeStockByBoxCode(boxCodes);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void unFreezeByLocCodeAction(String locCode, Long whId) {
		Location location = locationBiz.findLocationByLocCode(whId, locCode);
		AssertUtil.notNull(location, "????????????????????????????????????????????????????????????????????????");
		List<Long> locIds = new ArrayList<>();
		locIds.add(location.getLocId());
		stockBiz.unfreezeStockByLoc(locIds);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void unFreezeByLotNumberAction(String lotNumber) {
		AssertUtil.notEmpty(lotNumber, "????????????????????????????????????");
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		skuLot.setSkuLot1(lotNumber);
		List<Stock> stockList = stockQueryBiz.findEnableStockByZoneTypeAndSkuLot(skuLot);
		AssertUtil.notNull(stockList, "?????????????????????,???????????????????????????????????????");
		List<Long> stockIds = stockList.stream()
			.map(Stock::getStockId)
			.distinct()
			.collect(Collectors.toList());
		stockBiz.unfreezeStock(stockIds);
	}

	@Override
	public void unFreezeBySerialNumberAction(String serialNumber) {

	}

	@Override
	public void portionUnFreezeAction(PortionUnFreezeRequest request) {

	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void unFreezeStockByBoxCodeAction(String boxCode) {
		AssertUtil.notNull(boxCode, "??????????????????????????????");
		List<String> boxCodes = new ArrayList<>();
		boxCodes.add(boxCode);
		stockBiz.unFreezeStockByBoxCode(boxCodes);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void stockMove(StockMoveRequest request) {
		//??????skuCode??????SKU??????
		Sku sku = skuBiz.findByCode(request.getSkuCode());
		//??????locCode??????Location??????
		Location sourceLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		List<Long> locationIdList = new ArrayList<>();
		locationIdList.add(sourceLocation.getLocId());
		//????????????locCode??????Location??????
		Location targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getTargetLocCode());
		//???????????????
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		skuLot.setSkuLot1(request.getLotNumber());
		//????????????ID SKU_ID ?????? ????????????1??????????????????
		List<Stock> stockList = stockQueryBiz.findEnableStockByLocationAndSkuLot(request.getWhId(), sku.getSkuId(), null, locationIdList, skuLot);
		//??????stockList
		AssertUtil.notNull(stockList, "??????????????????????????????????????????????????????????????????????????????");
		//??????????????????????????????????????????????????????????????????????????????
		canMoveToSourceLocIsStageLocation(sourceLocation);
		//??????????????????????????????
		canMove(sourceLocation, targetLocation, stockList, stockList.get(0).getBoxCode());
		//????????????????????????????????????????????????????????????
		canMoveIsExceedSend(request.getQty(), stockList.get(0).getStockEnable());
		if (locationBiz.isAgvLocation(targetLocation)) {
			//AGV??????????????????
			agvTask.moveStockToSchedule(stockList, targetLocation);
			return;
		}

		//????????????
		stockBiz.moveStock(stockList.get(0), request.getSerialNumberList(), request.getQty(),
			targetLocation, StockLogTypeEnum.STOCK_MOVE_BY_PCS_PDA, null, null, null, null);
		//????????????
		logBiz.auditLog(AuditLogType.MOVE_STOCK,
			String.format("%s ??????[%s]???[%s]????????????[%s]???[%s]", StockLogTypeEnum.STOCK_MOVE_BY_PCS_PDA.getDesc(),
				stockList.get(0).getStockId(), stockList.get(0).getLocCode(), request.getQty(),
				targetLocation.getLocCode()));
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void stockMoveByLpn(StockMoveByLpnRequest request) {
		//????????????????????????LocCode
		Location targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getTargetLocCode());
		List<Stock> stockList = stockQueryBiz.findStockByLpnCodeOnStore(request.getLpnCode());
		AssertUtil.notNull(stockList, "LPN?????????????????????LPN????????????????????????");
		for (Stock stock : stockList) {
			if (StockStatusEnum.SYSTEM_FREEZE.equals(stock.getStockStatus())) {
				throw new ServiceException(String.format("???????????????????????????[%s]????????????????????????????????????,???????????????", stock.getBoxCode()));
			}
		}

		Location sourceLocation = locationBiz.findLocationByLocCode(stockList.get(0).getWhId(), stockList.get(0).getLocCode());
		//??????????????????????????????????????????????????????????????????????????????
		canMoveToSourceLocIsStageLocation(sourceLocation);
		canMove(sourceLocation, targetLocation, stockList, stockList.get(0).getBoxCode());
		if (locationBiz.isAgvLocation(targetLocation)) {
			//AGV??????????????????
			agvTask.moveStockToSchedule(stockList, targetLocation);
			return;
		}

		stockBiz.moveStockByLpnCode(request.getLpnCode(), request.getTargetLpnCode(), targetLocation,
			StockLogTypeEnum.STOCK_MOVE_BY_LPN_PDA, null, null, null, null);
		//????????????
		logBiz.auditLog(AuditLogType.MOVE_STOCK,
			String.format("%s LPN???[%s]?????????LPN???[%s]?????????[%s]", StockLogTypeEnum.STOCK_MOVE_BY_PCS_PDA.getDesc(),
				request.getLpnCode(), request.getTargetLpnCode(), targetLocation.getLocCode()));
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void stockMoveByBox(StockMoveByBoxCodeRequest request) {
		AssertUtil.notNull(request.getBoxCodeList(), "????????????????????????????????????");
		if (Func.isNotEmpty(request.getBoxCodeList())) {
			List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(request.getBoxCodeList());
			for (Stock stock : stockList) {
				if (StockStatusEnum.SYSTEM_FREEZE.equals(stock.getStockStatus())) {
					throw new ServiceException(String.format("???????????????????????????[%s]????????????????????????????????????,???????????????", stock.getBoxCode()));
				}
			}
		}
		//????????????????????????LocCode
		Location targetLocation;
		StockLogTypeEnum stockLogTypeEnum;
		if (Func.isNotEmpty(request.getLocCode())) {
			//????????????????????????LocCode
			targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
			stockLogTypeEnum = StockLogTypeEnum.STOCK_MOVE_BY_BOX_PDA;
		} else {
			//????????????????????????LocCode
			targetLocation = locationBiz.findByLocId(request.getTargetLocId());
			stockLogTypeEnum = StockLogTypeEnum.STOCK_MOVE_BY_BOX;
			AssertUtil.notNull(targetLocation, "??????????????????,???????????????ID?????????");
		}
		if (!targetLocation.enableStock()) {
			throw new ServiceException("?????????????????????????????????????????????????????????");
		}
		boolean replaceBoxCode = true;
		//?????????????????????????????????????????????????????????
		List<String> boxCodeList = request.getBoxCodeList().stream().filter(Func::isNotEmpty).collect(Collectors.toList());
		for (String boxCode : boxCodeList) {

			List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(boxCode);
			AssertUtil.notNull(stockList, "PDA????????????:?????????????????????????????????????????????????????????");
			if (stockList.size() == 0) {
				throw new ServiceException("PDA????????????:?????????????????????????????????????????????????????????");
			}
			if (Func.isEmpty(request.getLpnCode()) && replaceBoxCode) {
				request.setLpnCode(stockList.get(0).getLpnCode());
				replaceBoxCode = false;
			}
			Location sourceLocation = locationBiz.findLocationByLocCode(stockList.get(0).getWhId(), stockList.get(0).getLocCode());
			//??????????????????????????????
			canMove(sourceLocation, targetLocation, stockList, boxCode);
			if (zoneBiz.findById(sourceLocation.getZoneId()).getZoneType() == DictKVConstant.ZONE_TYPE_AGV_STORAGE) {
				//AGV??????????????????
				agvTask.moveStockToSchedule(stockList, targetLocation);
				break;
			}

			stockBiz.moveStockByBoxCode(boxCode, boxCode, request.getLpnCode(),
				targetLocation, stockLogTypeEnum, null, null, null, null);
			//????????????
			logBiz.auditLog(AuditLogType.MOVE_STOCK,
				String.format("%s ?????????[%s]??????????????????[%s]?????????[%s]", StockLogTypeEnum.STOCK_MOVE_BY_PCS_PDA.getDesc(),
					boxCode, boxCode, targetLocation.getLocCode()));
		}
	}

	@Override
	public EstimateStockMoveResponse skuIsSn(EstimateStockMoveRequest request) {
		//??????skuCode??????SKU??????
		Sku sku = skuBiz.findByCode(request.getSkuCode());
		//??????locCode??????Location??????
		Location sourceLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		List<Long> locationIdList = new ArrayList<>();
		locationIdList.add(sourceLocation.getLocId());
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		skuLot.setSkuLot1(request.getLotNumber());
		//????????????ID SKU_ID ?????? ????????????1??????????????????
		List<Stock> stockList = stockQueryBiz.findEnableStockByLocationAndSkuLot(request.getWhId(), sku.getSkuId(), null, locationIdList, skuLot);
		Stock stock = stockList.stream()
			.filter(stocks -> request.getLotNumber().equals(stocks.getSkuLot1()))
			.findFirst()
			.orElse(null);
		AssertUtil.notNull(stock, "????????????????????????????????????,??????????????????????????????");
		EstimateStockMoveResponse response = new EstimateStockMoveResponse();
		response.setIsSn(stock.isSerial());
		return response;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void stockFrozen(StockThawAndFrozenDto stockThawAndFrozenDto) {
		String msg = "";
		if (Func.isNotEmpty(stockThawAndFrozenDto.getBoxCodeList())) {
			List<String> boxCodeList = stockThawAndFrozenDto.getBoxCodeList()
				.stream()
				.distinct()
				.collect(Collectors.toList());
			stockBiz.freezeStockByBoxCode(boxCodeList);
			msg = String.format("????????????,??????:[%s]%s",
				String.join(",", boxCodeList),
				Func.isNotEmpty(stockThawAndFrozenDto.getRemark()) ? "??????:" + stockThawAndFrozenDto.getRemark() : "");
		}
		if (Func.isNotEmpty(stockThawAndFrozenDto.getLocIdList())) {
			List<Long> locIdList = stockThawAndFrozenDto.getLocIdList()
				.stream()
				.distinct()
				.map(Long::parseLong)
				.collect(Collectors.toList());
			stockBiz.freezeStockByLoc(locIdList);
			msg = String.format("???????????????,??????ID:[%s]%s",
				Func.join(locIdList),
				Func.isNotEmpty(stockThawAndFrozenDto.getRemark()) ? "??????:" + stockThawAndFrozenDto.getRemark() : "");
		}
		if (Func.isNotEmpty(stockThawAndFrozenDto.getSkuLot1List())) {
			List<Long> stockIdList = new ArrayList<>();
			List<String> skuLot1List = stockThawAndFrozenDto.getSkuLot1List()
				.stream()
				.distinct()
				.collect(Collectors.toList());
			for (String skuLot1 : skuLot1List) {
				SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
				skuLot.setSkuLot1(skuLot1);
				List<Stock> stockList = stockQueryBiz.findEnableStockByZoneTypeAndSkuLot(skuLot);
				List<Long> stockIds = stockList.stream().map(Stock::getStockId).collect(Collectors.toList());
				stockIdList.addAll(stockIds);
			}
			stockBiz.freezeStock(stockIdList);
			msg = String.format("?????????????????????,??????:[%s]%s",
				String.join(",", skuLot1List),
				Func.isNotEmpty(stockThawAndFrozenDto.getRemark()) ? "??????:" + stockThawAndFrozenDto.getRemark() : "");
		}
		logBiz.auditLog(AuditLogType.STOCK_TYPE, msg);

	}

	@Override
	public void stockUnFrozen(StockThawAndFrozenDto stockThawAndFrozenDto) {
		String msg = "";
		if (Func.isNotEmpty(stockThawAndFrozenDto.getBoxCodeList())) {
			List<String> boxCodeList = stockThawAndFrozenDto.getBoxCodeList()
				.stream()
				.distinct()
				.collect(Collectors.toList());
			stockBiz.unFreezeStockByBoxCode(boxCodeList);
			msg = String.format("????????????,??????:[%s]%s",
				String.join(",", boxCodeList),
				Func.isNotEmpty(stockThawAndFrozenDto.getRemark()) ? "??????:" + stockThawAndFrozenDto.getRemark() : "");
		}
		if (Func.isNotEmpty(stockThawAndFrozenDto.getLocIdList())) {
			List<Long> locIdList = stockThawAndFrozenDto.getLocIdList()
				.stream()
				.distinct()
				.map(Long::parseLong)
				.collect(Collectors.toList());
			stockBiz.unfreezeStockByLoc(locIdList);
			msg = String.format("???????????????,??????ID:[%s]%s",
				Func.join(locIdList),
				Func.isNotEmpty(stockThawAndFrozenDto.getRemark()) ? "??????:" + stockThawAndFrozenDto.getRemark() : "");
		}
		if (Func.isNotEmpty(stockThawAndFrozenDto.getSkuLot1List())) {
			List<Long> stockIdList = new ArrayList<>();
			List<String> skuLot1List = stockThawAndFrozenDto.getSkuLot1List()
				.stream()
				.distinct()
				.collect(Collectors.toList());
			for (String skuLot1 : skuLot1List) {
				SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
				skuLot.setSkuLot1(skuLot1);
				List<Stock> stockList = stockQueryBiz.findEnableStockByZoneTypeAndSkuLot(skuLot);
				List<Long> stockIds = stockList.stream().map(Stock::getStockId).collect(Collectors.toList());
				stockIdList.addAll(stockIds);
			}
			stockBiz.unfreezeStock(stockIdList);
			msg = String.format("?????????????????????,??????:[%s]%s",
				String.join(",", skuLot1List),
				Func.isNotEmpty(stockThawAndFrozenDto.getRemark()) ? "??????:" + stockThawAndFrozenDto.getRemark() : "");
		}
		logBiz.auditLog(AuditLogType.STOCK_TYPE, msg);
	}

	@Override
	public void decideStockLpn(String lpnCode) {
		List<Stock> stockList = stockQueryBiz.findStockByLpnCode(lpnCode);
		AssertUtil.notNull(stockList, "???Lpn????????????????????????,?????????LPN?????????");
	}

	@Override
	public void stockMoveByPc(StockPcMoveRequest stockPcMoveRequest) {
		Long stockId = stockPcMoveRequest.getStockId();
		Stock stock = stockQueryBiz.findStockById(stockId);
		List<StockPcMoveDetailRequest> stockMoveDataList = stockPcMoveRequest.getStockMoveDataList();
		stockMoveDataList.forEach(move -> {
			Location location = locationBiz.findByLocId(move.getLocId());
			canMoveToLoc(location);
			stockBiz.moveStock(stock, move.getSerials(), move.getQty(), location,
				StockLogTypeEnum.STOCK_MOVE_BY_PCS, null, null, null, null);
		});
	}

	/**
	 * ????????????????????????????????????????????????
	 * 1. ?????????????????????????????????????????????
	 * 2. ????????????????????????????????????????????????????????????????????????????????????
	 * 3. ???????????????????????????????????????????????????????????????
	 * 4. ???????????????????????????
	 * 5. ????????????
	 *
	 * @param sourceLocation sourceLocation
	 * @param targetLocation targetLocation
	 * @param stockList      stockList
	 * @param boxCode        boxCode
	 */
	@Override
	public void canMove(Location sourceLocation, Location targetLocation, List<Stock> stockList,
						String boxCode) {
		AssertUtil.notNull(sourceLocation, "??????????????????????????????????????????");
		AssertUtil.notNull(targetLocation, "??????????????????????????????????????????");
		AssertUtil.notNull(stockList, "????????????????????????????????????");
		AssertUtil.notNull(boxCode, "????????????????????????????????????");
		if (stockList.size() < 1) {
			throw new ServiceException("????????????????????????????????????");
		}

		//1. ?????????????????????????????????????????????
		Integer soucreZoneType = locationBiz.getZoneTypeByLocId(sourceLocation.getLocId());
		Integer targetZoneType = locationBiz.getZoneTypeByLocId(targetLocation.getLocId());
		//2. ???????????????????????????????????????
		checkTargetZoneType(soucreZoneType, targetZoneType, sourceLocation, targetLocation);
		//3. ??????????????????????????????????????????????????????????????????????????????????????????????????????
		canMove(targetLocation);
		//4. ???????????????????????????
		canMoveToBoxType(targetLocation, boxCode);
		// 5. ????????????
		canMoveByIsNotOverweight(targetLocation, stockList);
	}

	private void canMove(Location targetLocation) {
		if (locationBiz.isAgvLocation(targetLocation) || locationBiz.isAgvTemporaryOutLocation(targetLocation)) {
			if (!stockQueryBiz.isEmptyLocation(targetLocation.getLocId())) {
				throw new ServiceException("?????????????????????????????????????????????");
			}
		}

	}

	private void checkTargetZoneType(Integer soucreZoneType, Integer targetZoneType, Location sourceLocation, Location targetLocation) {
		// ???????????????AGV??????????????????????????????????????????D?????????????????????AGV?????????
		if (soucreZoneType == DictKVConstant.ZONE_TYPE_AGV_STORAGE) {
			if (!(locationBiz.isAgvTemporaryOutLocation(targetLocation) || locationBiz.isAgvLocation(targetLocation))) {
				throw new ServiceException("??????????????????,??????????????????????????????????????????");
			}
		}
		// ???????????????????????????????????????????????????????????????
		else if (locationBiz.isAgvTemporaryOutLocation(sourceLocation)) {
			if (!(targetZoneType == DictKVConstant.ZONE_TYPE_STAGE
				|| targetZoneType == DictKVConstant.ZONE_TYPE_PICK
				|| targetZoneType == DictKVConstant.ZONE_TYPE_AGV_PICK)) {
				throw new ServiceException("???????????????????????????????????????????????????????????????");
			}
		}
		// ???????????????????????????D???????????????????????????????????????D????????????
		else if (soucreZoneType == DictKVConstant.ZONE_TYPE_AGV_PICK || soucreZoneType == DictKVConstant.ZONE_TYPE_PICK) {
			if (!(targetZoneType == DictKVConstant.ZONE_TYPE_AGV_PICK || targetZoneType == DictKVConstant.ZONE_TYPE_PICK)) {
				throw new ServiceException("??????????????????,??????????????????????????????????????????");
			} else if (soucreZoneType == DictKVConstant.ZONE_TYPE_AGV_TEMPORARY) {
				if (!targetLocation.getZCode().equals("WH1-RECE")) {
					throw new ServiceException("??????????????????,??????????????????????????????????????????");
				}
			}
		} else if (soucreZoneType == DictKVConstant.ZONE_TYPE_VIRTUAL) {
			return;
		} else {
			throw new ServiceException("????????????????????????????????????????????????");
		}
	}

	@Override
	public void canMoveToBoxType(Location targetLocation, Location sourceLocation) {
		if (!locationBiz.isAgvTemporaryLocation(sourceLocation) && !locationBiz.isAgvTemporaryLocation(targetLocation)) {
			if (Func.isNotEmpty(targetLocation.getLpnTypeId()) && Func.isNotEmpty(sourceLocation.getLpnTypeId())) {
				LpnType sourceLpnType = lpnTypeBiz.findLpnTypeById(sourceLocation.getLpnTypeId());
				AssertUtil.notNull(sourceLpnType, "??????????????????????????????");
				LpnType targetLpnType = lpnTypeBiz.findLpnTypeById(targetLocation.getLpnTypeId());
				AssertUtil.notNull(targetLpnType, "??????????????????????????????");
				if (Func.isNotEmpty(targetLpnType.getCode()) && !Func.equals(sourceLpnType.getCode(), targetLpnType.getCode())) {
					throw new ServiceException("?????????????????????????????????????????????????????????????????????");
				}
			}
		}
	}

	/**
	 * ?????? ??????????????????????????????????????????????????????????????????
	 *
	 * @param targetLocation ????????????
	 */
	private void canMoveToLoc(Location targetLocation) {
		AssertUtil.notNull(targetLocation, "??????????????????????????????????????????");
		List<Location> outStockShippingLocationList = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_PICK_TO);
		Location outStockShippingLocation = outStockShippingLocationList
			.stream()
			.filter(location -> Func.equals(location.getLocId(), targetLocation.getLocId()))
			.findFirst()
			.orElse(null);
		if (Func.isNotEmpty(outStockShippingLocation)) {
			throw new ServiceException("?????????????????????????????????????????????");
		}
	}

	/**
	 * ????????????????????????????????????????????????????????????????????????????????????
	 *
	 * @param sourceLocation sourceLocation
	 * @param targetLocation targetLocation
	 */
	private void canMoveToLocAuto(Location sourceLocation, Location targetLocation) {
		if (locationBiz.isAgvLocation(sourceLocation) && locationBiz.isAgvLocation(targetLocation)) {
			if (!stockQueryBiz.isEmptyLocation(targetLocation.getLocId())) {
				throw new ServiceException("??????????????????????????????????????????????????????");
			}
		}
	}


	@Override
	public void canMoveToLocAuto(Location targetLocation) {
		if (locationBiz.isAgvLocation(targetLocation)) {
			if (!stockQueryBiz.isEmptyLocation(targetLocation.getLocId())) {
				throw new ServiceException("??????????????????????????????????????????????????????");
			}
		}
	}


	/**
	 * ???????????????????????????????????????
	 *
	 * @param sourceLocation sourceLocation
	 */
	private void canMoveToSourceLocIsStageLocation(Location sourceLocation) {
		if (locationBiz.isStageLocation(sourceLocation)) {
			throw new ServiceException("????????????????????????????????????????????????");
		}
	}

	/**
	 * ???????????????????????????????????????????????????????????????
	 *
	 * @param sourceLocation sourceLocation
	 * @param targetLocation targetLocation
	 */
	private void canMoveToLocType(Location sourceLocation, Location targetLocation) {
		if (locationBiz.isVirtualLocation(sourceLocation) || locationBiz.isVirtualLocation(targetLocation)) {
			return;
		}
		if (!Func.equals(sourceLocation.getZoneId(), targetLocation.getZoneId())) {
			throw new ServiceException("?????????????????????????????????");
		}

	}

	/**
	 * ???????????????????????????
	 *
	 * @param targetLocation targetLocation
	 * @param boxCode        boxCode
	 */
	@Override
	public void canMoveToBoxType(Location targetLocation, String boxCode) {
		if (Func.isNotEmpty(targetLocation.getLpnTypeId())) {
			AssertUtil.notEmpty(boxCode, "?????????????????????????????????");
			LpnType sourceLpnType = lpnTypeBiz.findLpnTypeByBoxCode(boxCode);
			AssertUtil.notNull(sourceLpnType, "??????????????????????????????");
			LpnType targetLpnType = lpnTypeBiz.findLpnTypeById(targetLocation.getLpnTypeId());
			AssertUtil.notNull(targetLpnType, "??????????????????????????????");
			if (Func.isNotEmpty(targetLpnType.getCode()) && !Func.equals(sourceLpnType.getCode(), targetLpnType.getCode())) {
				throw new ServiceException("?????????????????????????????????????????????????????????????????????");
			}
		}
	}

	/**
	 * ????????????
	 *
	 * @param targetLocation ????????????
	 * @param stockList      ????????????
	 */
	@Override
	public void canMoveByIsNotOverweight(Location targetLocation, List<Stock> stockList) {
		if (locationBiz.isAgvLocation(targetLocation)) {
			if (!tianYiPutawayStrategy.isNotOverweight(stockList, targetLocation)) {
				throw new ServiceException("???????????????????????????????????????");
			}
		}
	}

	/**
	 * ??????????????????????????????????????????????????????
	 *
	 * @param moveNumber ????????????
	 * @param stockQty   ????????????
	 */
	private void canMoveIsExceedSend(BigDecimal moveNumber, BigDecimal stockQty) {
		if (BigDecimalUtil.gt(moveNumber, stockQty)) {
			throw new ServiceException("????????????????????????????????????????????????????????????");
		}
	}
}
