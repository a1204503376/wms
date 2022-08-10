package org.nodes.wms.biz.stockManage.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.core.constant.DictKVConstant;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.putway.strategy.TianYiPutwayStrategy;
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
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	private final TianYiPutwayStrategy tianYiPutwayStrategy;
	private final AgvTask agvTask;

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
		List<Stock> stockList = stockQueryBiz.findEnableStockBySkuLot(skuLot);
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
		List<Stock> stockList = stockQueryBiz.findEnableStockBySkuLot(skuLot);
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
		Location location = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		List<Long> locationIdList = new ArrayList<>();
		locationIdList.add(location.getLocId());
		//根据目标locCode获取Location对象
		Location targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getTargetLocCode());
		//批属性赋值
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		skuLot.setSkuLot1(request.getLotNumber());
		//根据库房ID SKU_ID 库位 和批属性1查询对应库存
		List<Stock> stockList = stockQueryBiz.findEnableStockByLocation(request.getWhId(), sku.getSkuId(), null, locationIdList, skuLot);
		//断言stockList
		AssertUtil.notNull(stockList, "根据您输入的数据查询不到对应的库存，请重新输入后重试");
		//判断库存是否可以移动
		canMove(location, targetLocation, stockList, stockList.get(0).getBoxCode());

		if (locationBiz.isAgvLocation(targetLocation)) {
			//AGV移动任务生成
			agvTask.moveStockToSchedule(stockList, targetLocation.getLocId());
			return;
		}

		//库存移动
		stockBiz.moveStock(stockList.get(0), request.getSerialNumberList(), stockList.get(0).getStockBalance(), targetLocation, StockLogTypeEnum.STOCK_MOVE_BY_PCS_PDA, null, null, null);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void stockMoveByLpn(StockMoveByLpnRequest request) {
		//根据前端传过来的LocCode
		Location targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getTargetLocCode());
		List<Stock> stockList = stockQueryBiz.findStockByLpnCode(request.getLpnCode());
		AssertUtil.notNull(stockList, "LPN移动失败，根据LPN获取库存集合为空");
		Location sourceLocation = locationBiz.findLocationByLocCode(stockList.get(0).getWhId(), stockList.get(0).getLocCode());
		canMove(sourceLocation, targetLocation, stockList, stockList.get(0).getBoxCode());
		if (locationBiz.isAgvLocation(targetLocation)) {
			//AGV移动任务生成
			agvTask.moveStockToSchedule(stockList, targetLocation.getLocId());
			return;
		}

		stockBiz.moveStockByLpnCode(request.getLpnCode(), request.getTargetLpnCode(), targetLocation, StockLogTypeEnum.STOCK_MOVE_BY_LPN_PDA, null, null, null);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void stockMoveByBox(StockMoveByBoxCodeRequest request) {
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
		if (Func.isEmpty(request.getLpnCode())) {
			request.setLpnCode(targetLocation.getLocCode());
		}
		//根据传过来的多个箱码集合查询出多个库存
		List<String> boxCodeList = request.getBoxCodeList().stream().filter(Func::isNotEmpty).collect(Collectors.toList());
		boxCodeList.forEach(boxCode -> {
				List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(boxCode);
				AssertUtil.notNull(stockList, "PDA库存管理:按箱移动失败，根据箱码查询不到对应库存");
				Location location = locationBiz.findLocationByLocCode(stockList.get(0).getWhId(), stockList.get(0).getLocCode());
				//判断库存是否可以移动
				canMove(location, targetLocation, stockList, boxCode);
				if (locationBiz.isAgvLocation(targetLocation)) {
					//AGV移动任务生成
					agvTask.moveStockToSchedule(stockList, targetLocation.getLocId());
					return;
				}
				stockBiz.moveStockByBoxCode(boxCode, boxCode, request.getLpnCode(),
					targetLocation, stockLogTypeEnum, null, null, null);
			}
		);
	}

	@Override
	public EstimateStockMoveResponse skuIsSn(EstimateStockMoveRequest request) {
		//根据skuCode获取SKU对象
		Sku sku = skuBiz.findByCode(request.getSkuCode());
		EstimateStockMoveResponse response = new EstimateStockMoveResponse();
		response.setIsSn(sku.getIsSn() > 0);
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
				List<Stock> stockList = stockQueryBiz.findEnableStockBySkuLot(skuLot);
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
				List<Stock> stockList = stockQueryBiz.findEnableStockBySkuLot(skuLot);
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
				StockLogTypeEnum.STOCK_MOVE_BY_PCS, null, null, null);
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
	public void canMove(Location sourceLocation, Location targetLocation, List<Stock> stockList, String boxCode) {
		AssertUtil.notNull(sourceLocation, "校验库存移动失败当前库位为空");
		AssertUtil.notNull(targetLocation, "校验库存移动失败目标库位为空");
		AssertUtil.notNull(stockList, "校验库存移动失败库存为空");

		//1. 不能移动到出库集货区和虚拟库区
		canMoveToLoc(targetLocation);

		//2. 如果是自动区则要求目标库位必须是空库位（库位上没有库存）
		canMoveToLocAuto(sourceLocation, targetLocation);

		//3. 只能是同类型（自动与人工区）的库区之间移动
		canMoveToLocType(sourceLocation, targetLocation);

		//4. 校验目标库位的箱型
		canMoveToBoxType(targetLocation, boxCode);

		// 5. 校验载重
		canMoveByIsNotOverweight(targetLocation, stockList);
	}

	/**
	 * 校验 库存移动：不能移动到出库集货区、虚拟区的库位
	 *
	 * @param targetLocation 目标库存
	 */
	private void canMoveToLoc(Location targetLocation) {
		AssertUtil.notNull(targetLocation, "校验库存移动失败目标库位为空");
		List<Location> outStockShippingLocationList = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_OF_PICK_TO);
		Location outStockShippingLocation = outStockShippingLocationList
			.stream()
			.filter(location -> Func.equals(location.getLocId(), targetLocation.getLocId()))
			.findFirst()
			.orElse(null);
		if (Func.isNotEmpty(outStockShippingLocation)) {
			throw new ServiceException("库存移动时不能移动到出库集货区");
		}

		List<Location> virtualLocationList = locationBiz.getLocationByZoneType(DictKVConstant.ZONE_TYPE_VIRTUAL_AREA);
		Location virtualLocation = virtualLocationList.stream()
			.filter(location -> Func.equals(location.getLocId(), targetLocation.getLocId()))
			.findFirst()
			.orElse(null);
		if (Func.isNotEmpty(virtualLocation)) {
			throw new ServiceException("库存移动时不能移动到虚拟区");
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

	/**
	 * 只能是同类型（自动与人工区）的库区之间移动
	 *
	 * @param sourceLocation sourceLocation
	 * @param targetLocation targetLocation
	 */
	private void canMoveToLocType(Location sourceLocation, Location targetLocation) {
		if (!Func.equals(sourceLocation.getZoneId(), targetLocation.getZoneId()) && !locationBiz.isStageLocation(sourceLocation)) {
			throw new ServiceException("库存移动时不能跨区移动");
		}
	}

	/**
	 * 校验目标库位的箱型
	 *
	 * @param targetLocation targetLocation
	 * @param boxCode        boxCode
	 */
	private void canMoveToBoxType(Location targetLocation, String boxCode) {
		if (Func.isNotEmpty(targetLocation.getLpnTypeId())) {
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
	private void canMoveByIsNotOverweight(Location targetLocation, List<Stock> stockList) {
		if (!tianYiPutwayStrategy.isNotOverweight(stockList, targetLocation)) {
			throw new ServiceException("要移动的库存超过了最大载重");
		}
	}
}
