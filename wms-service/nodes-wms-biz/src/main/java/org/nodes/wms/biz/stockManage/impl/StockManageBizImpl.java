package org.nodes.wms.biz.stockManage.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.stockManage.StockManageBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.stock.dto.input.*;
import org.nodes.wms.dao.stock.dto.output.EstimateStockMoveResponse;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
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
		// TODO库存标准移动没写全
		//根据skuCode获取SKU对象
		Sku sku = skuBiz.findByCode(request.getSkuCode());
		//根据locCode获取Location对象
		Location location = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		//根据目标locCode获取Location对象
		Location targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getTargetLocCode());
		List<Long> locationIdList = new ArrayList<>();
		locationIdList.add(location.getLocId());
		//批属性赋值
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		skuLot.setSkuLot1(request.getLotNumber());
		List<Stock> stockList = stockQueryBiz.findEnableStockByLocation(request.getWhId(), sku.getSkuId(), null, locationIdList, skuLot);
		AssertUtil.notNull(stockList, "根据您输入的数据查询不到对应的库存，请重新输入后重试");
		stockList.forEach(stock -> {
			//获取序列号 如果库存关联了序列号需要获取序列号
			List<Serial> serialList = stockQueryBiz.findSerialByStock(stock.getStockId());
			List<String> serialNoList = null;
			if (Func.isNotEmpty(serialList)) {
				serialNoList = serialList.stream()
					.map(Serial::getSerialNumber)
					.collect(Collectors.toList());
			}
			stockBiz.moveStock(stock, serialNoList, stock.getStockBalance(), targetLocation, StockLogTypeEnum.STOCK_MOVE_BY_PCS_PDA, null, null, null);
		});

		//根据请求对象获取对应库存移动需要的值
		//根据库存ID获取原库存
		//获取序列号集合 移动数量,库存移动类型
		//获取目标库位信息
	}


	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void stockMoveByLpn(StockMoveByLpnRequest request) {
		//根据前端传过来的LocCode
		Location targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getTargetLocCode());
		stockBiz.moveStockByLpnCode(request.getLpnCode(), request.getTargetLpnCode(), targetLocation, StockLogTypeEnum.STOCK_MOVE_BY_LPN_PDA, null, null, null);
	}


	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void stockMoveByBox(StockMoveByBoxCodeRequest request) {
		//根据前端传过来的LocCode
		Location targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		if (Func.isEmpty(request.getLpnCode())) {
			request.setLpnCode(targetLocation.getLocCode());
		}
		//根据传过来的多个箱码集合查询出多个库存
		List<String> boxCodeList = request.getBoxCodeList().stream().filter(Func::isNotEmpty).collect(Collectors.toList());
		List<Stock> stockList = stockQueryBiz.findStockMoveByBoxCode(boxCodeList);
		for (Stock stock : stockList) {
			System.out.println("stock.getBoxCode()" + stock.getBoxCode());
			//移动
			stockBiz.moveStock(stock, null, stock.getStockBalance(), stock.getBoxCode(), request.getLpnCode(), targetLocation, StockLogTypeEnum.STOCK_MOVE_BY_BOX_PDA, null, null, null);
		}
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
}
