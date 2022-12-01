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
		AssertUtil.notNull(location, "根据库位编码冻结时，根据库房编码查询出的库位为空");
		List<Long> locIds = new ArrayList<>();
		locIds.add(location.getLocId());
		stockBiz.freezeStockByLoc(locIds);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void freezeByLotNumberAction(String lotNumber) {
		AssertUtil.notEmpty(lotNumber, "按批次号冻结时批次号为空");
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		skuLot.setSkuLot1(lotNumber);
		List<Stock> stockList = stockQueryBiz.findEnableStockByZoneTypeAndSkuLot(skuLot);
		AssertUtil.notNull(stockList, "按批次号冻结时,根据批次号查询不到对应库存");
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
		AssertUtil.notNull(boxCode, "按箱冻结时箱码未输入");
		List<String> boxCodes = new ArrayList<>();
		boxCodes.add(boxCode);
		stockBiz.freezeStockByBoxCode(boxCodes);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void unFreezeByLocCodeAction(String locCode, Long whId) {
		Location location = locationBiz.findLocationByLocCode(whId, locCode);
		AssertUtil.notNull(location, "根据库位编码解冻时，根据库房编码查询出的库位为空");
		List<Long> locIds = new ArrayList<>();
		locIds.add(location.getLocId());
		stockBiz.unfreezeStockByLoc(locIds);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void unFreezeByLotNumberAction(String lotNumber) {
		AssertUtil.notEmpty(lotNumber, "按批次号解冻时批次号为空");
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		skuLot.setSkuLot1(lotNumber);
		List<Stock> stockList = stockQueryBiz.findEnableStockByZoneTypeAndSkuLot(skuLot);
		AssertUtil.notNull(stockList, "按批次号解冻时,根据批次号查询不到对应库存");
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
		AssertUtil.notNull(boxCode, "按箱解冻时箱码未输入");
		List<String> boxCodes = new ArrayList<>();
		boxCodes.add(boxCode);
		stockBiz.unFreezeStockByBoxCode(boxCodes);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void stockMove(StockMoveRequest request) {
		//根据skuCode获取SKU对象
		Sku sku = skuBiz.findByCode(request.getSkuCode());
		//根据locCode获取Location对象
		Location sourceLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		List<Long> locationIdList = new ArrayList<>();
		locationIdList.add(sourceLocation.getLocId());
		//根据目标locCode获取Location对象
		Location targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getTargetLocCode());
		//批属性赋值
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		skuLot.setSkuLot1(request.getLotNumber());
		//根据库房ID SKU_ID 库位 和批属性1查询对应库存
		List<Stock> stockList = stockQueryBiz.findEnableStockByLocationAndSkuLot(request.getWhId(), sku.getSkuId(), null, locationIdList, skuLot);
		//断言stockList
		AssertUtil.notNull(stockList, "根据您输入的数据查询不到对应的库存，请重新输入后重试");
		//判断原库存是否是入库暂存区，原库存移动时不允许暂存区
		canMoveToSourceLocIsStageLocation(sourceLocation);
		//判断库存是否可以移动
		canMove(sourceLocation, targetLocation, stockList, stockList.get(0).getBoxCode());
		//判断库存移动时：移动数量是否超过库存数量
		canMoveIsExceedSend(request.getQty(), stockList.get(0).getStockEnable());
		if (locationBiz.isAgvLocation(targetLocation)) {
			//AGV移动任务生成
			agvTask.moveStockToSchedule(stockList, targetLocation);
			return;
		}

		//库存移动
		stockBiz.moveStock(stockList.get(0), request.getSerialNumberList(), request.getQty(),
			targetLocation, StockLogTypeEnum.STOCK_MOVE_BY_PCS_PDA, null, null, null, null);
		//业务日志
		logBiz.auditLog(AuditLogType.MOVE_STOCK,
			String.format("%s 库存[%s]由[%s]移动数量[%s]到[%s]", StockLogTypeEnum.STOCK_MOVE_BY_PCS_PDA.getDesc(),
				stockList.get(0).getStockId(), stockList.get(0).getLocCode(), request.getQty(),
				targetLocation.getLocCode()));
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void stockMoveByLpn(StockMoveByLpnRequest request) {
		//根据前端传过来的LocCode
		Location targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getTargetLocCode());
		List<Stock> stockList = stockQueryBiz.findStockByLpnCodeOnStore(request.getLpnCode());
		AssertUtil.notNull(stockList, "LPN移动失败，根据LPN获取库存集合为空");
		for (Stock stock : stockList) {
			if (StockStatusEnum.SYSTEM_FREEZE.equals(stock.getStockStatus())) {
				throw new ServiceException(String.format("按箱移动失败，箱码[%s]的库存状态是系统冻结状态,不允许移动", stock.getBoxCode()));
			}
		}

		Location sourceLocation = locationBiz.findLocationByLocCode(stockList.get(0).getWhId(), stockList.get(0).getLocCode());
		//判断原库存是否是入库暂存区，原库存移动时不允许暂存区
		canMoveToSourceLocIsStageLocation(sourceLocation);
		canMove(sourceLocation, targetLocation, stockList, stockList.get(0).getBoxCode());
		if (locationBiz.isAgvLocation(targetLocation)) {
			//AGV移动任务生成
			agvTask.moveStockToSchedule(stockList, targetLocation);
			return;
		}

		stockBiz.moveStockByLpnCode(request.getLpnCode(), request.getTargetLpnCode(), targetLocation,
			StockLogTypeEnum.STOCK_MOVE_BY_LPN_PDA, null, null, null, null);
		//业务日志
		logBiz.auditLog(AuditLogType.MOVE_STOCK,
			String.format("%s LPN为[%s]移动到LPN为[%s]的库位[%s]", StockLogTypeEnum.STOCK_MOVE_BY_PCS_PDA.getDesc(),
				request.getLpnCode(), request.getTargetLpnCode(), targetLocation.getLocCode()));
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void stockMoveByBox(StockMoveByBoxCodeRequest request) {
		AssertUtil.notNull(request.getBoxCodeList(), "按箱移动失败，请输入箱码");
		if (Func.isNotEmpty(request.getBoxCodeList())) {
			List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(request.getBoxCodeList());
			for (Stock stock : stockList) {
				if (StockStatusEnum.SYSTEM_FREEZE.equals(stock.getStockStatus())) {
					throw new ServiceException(String.format("按箱移动失败，箱码[%s]的库存状态是系统冻结状态,不允许移动", stock.getBoxCode()));
				}
			}
		}
		//根据前端传过来的LocCode
		Location targetLocation;
		StockLogTypeEnum stockLogTypeEnum;
		if (Func.isNotEmpty(request.getLocCode())) {
			//根据前端传过来的LocCode
			targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
			stockLogTypeEnum = StockLogTypeEnum.STOCK_MOVE_BY_BOX_PDA;
		} else {
			//根据前端传过来的LocCode
			targetLocation = locationBiz.findByLocId(request.getTargetLocId());
			stockLogTypeEnum = StockLogTypeEnum.STOCK_MOVE_BY_BOX;
			AssertUtil.notNull(targetLocation, "获取库位失败,请更换库位ID后重试");
		}
		if (!targetLocation.enableStock()) {
			throw new ServiceException("按箱移动失败，目标库位状态不是正常状态");
		}
		boolean replaceBoxCode = true;
		//根据传过来的多个箱码集合查询出多个库存
		List<String> boxCodeList = request.getBoxCodeList().stream().filter(Func::isNotEmpty).collect(Collectors.toList());
		for (String boxCode : boxCodeList) {

			List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(boxCode);
			AssertUtil.notNull(stockList, "PDA库存管理:按箱移动失败，根据箱码查询不到对应库存");
			if (stockList.size() == 0) {
				throw new ServiceException("PDA库存管理:按箱移动失败，根据箱码查询不到对应库存");
			}
			if (Func.isEmpty(request.getLpnCode()) && replaceBoxCode) {
				request.setLpnCode(stockList.get(0).getLpnCode());
				replaceBoxCode = false;
			}
			Location sourceLocation = locationBiz.findLocationByLocCode(stockList.get(0).getWhId(), stockList.get(0).getLocCode());
			//判断库存是否可以移动
			canMove(sourceLocation, targetLocation, stockList, boxCode);
			if (zoneBiz.findById(sourceLocation.getZoneId()).getZoneType() == DictKVConstant.ZONE_TYPE_AGV_STORAGE) {
				//AGV移动任务生成
				agvTask.moveStockToSchedule(stockList, targetLocation);
				break;
			}

			stockBiz.moveStockByBoxCode(boxCode, boxCode, request.getLpnCode(),
				targetLocation, stockLogTypeEnum, null, null, null, null);
			//业务日志
			logBiz.auditLog(AuditLogType.MOVE_STOCK,
				String.format("%s 箱码为[%s]移动到箱码为[%s]的库位[%s]", StockLogTypeEnum.STOCK_MOVE_BY_PCS_PDA.getDesc(),
					boxCode, boxCode, targetLocation.getLocCode()));
		}
	}

	@Override
	public EstimateStockMoveResponse skuIsSn(EstimateStockMoveRequest request) {
		//根据skuCode获取SKU对象
		Sku sku = skuBiz.findByCode(request.getSkuCode());
		//根据locCode获取Location对象
		Location sourceLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		List<Long> locationIdList = new ArrayList<>();
		locationIdList.add(sourceLocation.getLocId());
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		skuLot.setSkuLot1(request.getLotNumber());
		//根据库房ID SKU_ID 库位 和批属性1查询对应库存
		List<Stock> stockList = stockQueryBiz.findEnableStockByLocationAndSkuLot(request.getWhId(), sku.getSkuId(), null, locationIdList, skuLot);
		Stock stock = stockList.stream()
			.filter(stocks -> request.getLotNumber().equals(stocks.getSkuLot1()))
			.findFirst()
			.orElse(null);
		AssertUtil.notNull(stock, "查询失败，找不到对应库存,请确认数据正确后重试");
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
			msg = String.format("按箱冻结,箱号:[%s]%s",
				String.join(",", boxCodeList),
				Func.isNotEmpty(stockThawAndFrozenDto.getRemark()) ? "备注:" + stockThawAndFrozenDto.getRemark() : "");
		}
		if (Func.isNotEmpty(stockThawAndFrozenDto.getLocIdList())) {
			List<Long> locIdList = stockThawAndFrozenDto.getLocIdList()
				.stream()
				.distinct()
				.map(Long::parseLong)
				.collect(Collectors.toList());
			stockBiz.freezeStockByLoc(locIdList);
			msg = String.format("按库位冻结,库存ID:[%s]%s",
				Func.join(locIdList),
				Func.isNotEmpty(stockThawAndFrozenDto.getRemark()) ? "备注:" + stockThawAndFrozenDto.getRemark() : "");
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
			msg = String.format("按生产批次冻结,批次:[%s]%s",
				String.join(",", skuLot1List),
				Func.isNotEmpty(stockThawAndFrozenDto.getRemark()) ? "备注:" + stockThawAndFrozenDto.getRemark() : "");
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
			msg = String.format("按箱解冻,箱号:[%s]%s",
				String.join(",", boxCodeList),
				Func.isNotEmpty(stockThawAndFrozenDto.getRemark()) ? "备注:" + stockThawAndFrozenDto.getRemark() : "");
		}
		if (Func.isNotEmpty(stockThawAndFrozenDto.getLocIdList())) {
			List<Long> locIdList = stockThawAndFrozenDto.getLocIdList()
				.stream()
				.distinct()
				.map(Long::parseLong)
				.collect(Collectors.toList());
			stockBiz.unfreezeStockByLoc(locIdList);
			msg = String.format("按库位解冻,库位ID:[%s]%s",
				Func.join(locIdList),
				Func.isNotEmpty(stockThawAndFrozenDto.getRemark()) ? "备注:" + stockThawAndFrozenDto.getRemark() : "");
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
			msg = String.format("按生产批次解冻,批次:[%s]%s",
				String.join(",", skuLot1List),
				Func.isNotEmpty(stockThawAndFrozenDto.getRemark()) ? "备注:" + stockThawAndFrozenDto.getRemark() : "");
		}
		logBiz.auditLog(AuditLogType.STOCK_TYPE, msg);
	}

	@Override
	public void decideStockLpn(String lpnCode) {
		List<Stock> stockList = stockQueryBiz.findStockByLpnCode(lpnCode);
		AssertUtil.notNull(stockList, "该Lpn查询不到对应库存,请更换LPN后重试");
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
	 * 判断库存是否可以移动（天宜定制）
	 * 1. 不能移动到出库集货区和虚拟库区
	 * 2. 如果是自动区则要求目标库位必须是空库位（库位上没有库存）
	 * 3. 只能是同类型（自动与人工区）的库区之间移动
	 * 4. 校验目标库位的箱型
	 * 5. 校验载重
	 *
	 * @param sourceLocation sourceLocation
	 * @param targetLocation targetLocation
	 * @param stockList      stockList
	 * @param boxCode        boxCode
	 */
	@Override
	public void canMove(Location sourceLocation, Location targetLocation, List<Stock> stockList,
						String boxCode) {
		AssertUtil.notNull(sourceLocation, "校验库存移动失败当前库位为空");
		AssertUtil.notNull(targetLocation, "校验库存移动失败目标库位为空");
		AssertUtil.notNull(stockList, "校验库存移动失败库存为空");
		AssertUtil.notNull(boxCode, "校验库存移动失败箱码为空");
		if (stockList.size() < 1) {
			throw new ServiceException("校验库存移动失败库存为空");
		}

		//1. 获取来源库位和目标库位库区类型
		Integer soucreZoneType = locationBiz.getZoneTypeByLocId(sourceLocation.getLocId());
		Integer targetZoneType = locationBiz.getZoneTypeByLocId(targetLocation.getLocId());
		//2. 判断是否可以移动到目标库位
		checkTargetZoneType(soucreZoneType, targetZoneType, sourceLocation, targetLocation);
		//3. 如果是自动区、出库接驳区则要求目标库位必须是空库位（库位上没有库存）
		canMove(targetLocation);
		//4. 校验目标库位的箱型
		canMoveToBoxType(targetLocation, boxCode);
		// 5. 校验载重
		canMoveByIsNotOverweight(targetLocation, stockList);
	}

	private void canMove(Location targetLocation) {
		if (locationBiz.isAgvLocation(targetLocation) || locationBiz.isAgvTemporaryOutLocation(targetLocation)) {
			if (!stockQueryBiz.isEmptyLocation(targetLocation.getLocId())) {
				throw new ServiceException("库存移动失败，目标库位存在库存");
			}
		}

	}

	private void checkTargetZoneType(Integer soucreZoneType, Integer targetZoneType, Location sourceLocation, Location targetLocation) {
		// 来源库位为AGV自动区只能移动到出库接驳区、D箱人工拣货区、AGV自动区
		if (soucreZoneType == DictKVConstant.ZONE_TYPE_AGV_STORAGE) {
			if (!(locationBiz.isAgvTemporaryOutLocation(targetLocation) || locationBiz.isAgvLocation(targetLocation))) {
				throw new ServiceException("库存移动失败,来源库位不允许移动到目标库位");
			}
		}
		// 来源库位为出库接驳区，只能移动到入库暂存区
		else if (locationBiz.isAgvTemporaryOutLocation(sourceLocation)) {
			if (!(targetZoneType == DictKVConstant.ZONE_TYPE_STAGE
				|| targetZoneType == DictKVConstant.ZONE_TYPE_PICK
				|| targetZoneType == DictKVConstant.ZONE_TYPE_AGV_PICK)) {
				throw new ServiceException("库存移动失败，来源库位不允许移动到目标库位");
			}
		}
		// 来源库位为人工区或D箱人工区只能移动到人工区或D箱人工区
		else if (soucreZoneType == DictKVConstant.ZONE_TYPE_AGV_PICK || soucreZoneType == DictKVConstant.ZONE_TYPE_PICK) {
			if (!(targetZoneType == DictKVConstant.ZONE_TYPE_AGV_PICK || targetZoneType == DictKVConstant.ZONE_TYPE_PICK)) {
				throw new ServiceException("库存移动失败,来源库位不允许移动到目标库位");
			}
		} else if (soucreZoneType == DictKVConstant.ZONE_TYPE_VIRTUAL) {
			return;
		} else {
			throw new ServiceException("库存移动失败，来源库位不允许移动");
		}
	}

	@Override
	public void canMoveToBoxType(Location targetLocation, Location sourceLocation) {
		if (!locationBiz.isAgvTemporaryLocation(sourceLocation) && !locationBiz.isAgvTemporaryLocation(targetLocation)) {
			if (Func.isNotEmpty(targetLocation.getLpnTypeId()) && Func.isNotEmpty(sourceLocation.getLpnTypeId())) {
				LpnType sourceLpnType = lpnTypeBiz.findLpnTypeById(sourceLocation.getLpnTypeId());
				AssertUtil.notNull(sourceLpnType, "获取当前箱子箱型失败");
				LpnType targetLpnType = lpnTypeBiz.findLpnTypeById(targetLocation.getLpnTypeId());
				AssertUtil.notNull(targetLpnType, "获取目标库位箱型失败");
				if (Func.isNotEmpty(targetLpnType.getCode()) && !Func.equals(sourceLpnType.getCode(), targetLpnType.getCode())) {
					throw new ServiceException("库存移动时当前库存和目标库位所存储的箱型不一致");
				}
			}
		}
	}

	/**
	 * 校验 库存移动：不能移动到出库集货区、虚拟区的库位
	 *
	 * @param targetLocation 目标库存
	 */
	private void canMoveToLoc(Location targetLocation) {
		AssertUtil.notNull(targetLocation, "校验库存移动失败目标库位为空");
		List<Location> outStockShippingLocationList = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_PICK_TO);
		Location outStockShippingLocation = outStockShippingLocationList
			.stream()
			.filter(location -> Func.equals(location.getLocId(), targetLocation.getLocId()))
			.findFirst()
			.orElse(null);
		if (Func.isNotEmpty(outStockShippingLocation)) {
			throw new ServiceException("库存移动时不能移动到出库集货区");
		}
	}

	/**
	 * 如果是自动区则要求目标库位必须是空库位（库位上没有库存）
	 *
	 * @param sourceLocation sourceLocation
	 * @param targetLocation targetLocation
	 */
	private void canMoveToLocAuto(Location sourceLocation, Location targetLocation) {
		if (locationBiz.isAgvLocation(sourceLocation) && locationBiz.isAgvLocation(targetLocation)) {
			if (!stockQueryBiz.isEmptyLocation(targetLocation.getLocId())) {
				throw new ServiceException("库存移动失败，自动区目标库位存在库存");
			}
		}
	}


	@Override
	public void canMoveToLocAuto(Location targetLocation) {
		if (locationBiz.isAgvLocation(targetLocation)) {
			if (!stockQueryBiz.isEmptyLocation(targetLocation.getLocId())) {
				throw new ServiceException("库存移动失败，自动区目标库位存在库存");
			}
		}
	}


	/**
	 * 移动时无法移动到出库暂存区
	 *
	 * @param sourceLocation sourceLocation
	 */
	private void canMoveToSourceLocIsStageLocation(Location sourceLocation) {
		if (locationBiz.isStageLocation(sourceLocation)) {
			throw new ServiceException("库存移动时原库存不能是入库暂存区");
		}
	}

	/**
	 * 只能是同类型（自动与人工区）的库区之间移动
	 *
	 * @param sourceLocation sourceLocation
	 * @param targetLocation targetLocation
	 */
	private void canMoveToLocType(Location sourceLocation, Location targetLocation) {
		if (locationBiz.isVirtualLocation(sourceLocation) || locationBiz.isVirtualLocation(targetLocation)) {
			return;
		}
		if (!Func.equals(sourceLocation.getZoneId(), targetLocation.getZoneId())) {
			throw new ServiceException("库存移动时不能跨区移动");
		}

	}

	/**
	 * 校验目标库位的箱型
	 *
	 * @param targetLocation targetLocation
	 * @param boxCode        boxCode
	 */
	@Override
	public void canMoveToBoxType(Location targetLocation, String boxCode) {
		if (Func.isNotEmpty(targetLocation.getLpnTypeId())) {
			AssertUtil.notEmpty(boxCode, "校验箱型失败，箱码为空");
			LpnType sourceLpnType = lpnTypeBiz.findLpnTypeByBoxCode(boxCode);
			AssertUtil.notNull(sourceLpnType, "获取当前箱子箱型失败");
			LpnType targetLpnType = lpnTypeBiz.findLpnTypeById(targetLocation.getLpnTypeId());
			AssertUtil.notNull(targetLpnType, "获取目标库位箱型失败");
			if (Func.isNotEmpty(targetLpnType.getCode()) && !Func.equals(sourceLpnType.getCode(), targetLpnType.getCode())) {
				throw new ServiceException("库存移动时当前库存和目标库位所存储的箱型不一致");
			}
		}
	}

	/**
	 * 校验载重
	 *
	 * @param targetLocation 目标库存
	 * @param stockList      库存集合
	 */
	@Override
	public void canMoveByIsNotOverweight(Location targetLocation, List<Stock> stockList) {
		if (locationBiz.isAgvLocation(targetLocation)) {
			if (!tianYiPutawayStrategy.isNotOverweight(stockList, targetLocation)) {
				throw new ServiceException("要移动的库存超过了最大载重");
			}
		}
	}

	/**
	 * 根据要移动的数量判断库存是否可以移动
	 *
	 * @param moveNumber 移动数量
	 * @param stockQty   库存数量
	 */
	private void canMoveIsExceedSend(BigDecimal moveNumber, BigDecimal stockQty) {
		if (BigDecimalUtil.gt(moveNumber, stockQty)) {
			throw new ServiceException("库存移动失败，要移动的数量超过了库存数量");
		}
	}
}
