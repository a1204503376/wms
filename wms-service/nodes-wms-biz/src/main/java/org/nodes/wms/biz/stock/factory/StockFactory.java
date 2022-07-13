package org.nodes.wms.biz.stock.factory;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.common.stock.StockUtil;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class StockFactory {
	private final SkuBiz skuBiz;
	private final ZoneBiz zoneBiz;

	public Stock create(ReceiveLog receiveLog, Location location){
		AssertUtil.notNull(receiveLog, "创建库存失败，清点记录为空");
		AssertUtil.notNull(location, "创建库存失败，目标为空");

		Stock stock = new Stock();
		SkuLotUtil.setAllSkuLot(receiveLog, stock);
		stock.setLastInTime(LocalDateTime.now());
		// TODO 如果没有库存状态则默认
		stock.setStockStatus(StockStatusEnum.NORMAL);
		stock.setSkuLevel(receiveLog.getSkuLevel());
		stock.setWsuCode(receiveLog.getWsuCode());
		Sku sku = skuBiz.findById(receiveLog.getSkuId());
		if (Func.isNotEmpty(sku)) {
			stock.setWspName(sku.getWspName());
		}
		stock.setWspId(receiveLog.getWspId());
		stock.setSkuId(receiveLog.getSkuId());
		stock.setSkuCode(receiveLog.getSkuCode());
		stock.setSkuName(receiveLog.getSkuName());
		stock.setStayStockQty(BigDecimal.ZERO);
		stock.setStockQty(receiveLog.getQty());
		stock.setPickQty(BigDecimal.ZERO);
		stock.setOccupyQty(BigDecimal.ZERO);
		stock.setBoxCode(receiveLog.getBoxCode());
		stock.setLpnCode(receiveLog.getLpnCode());
		stock.setLocCode(receiveLog.getLocCode());
		stock.setLocId(receiveLog.getLocId());
		stock.setZoneId(location.getZoneId());
		Zone zone = zoneBiz.findById(location.getZoneId());
		if (Func.isNotEmpty(zone)) {
			stock.setZoneCode(zone.getZoneCode());
		}
		stock.setWhId(receiveLog.getWhId());
		stock.setWhCode(receiveLog.getWhCode());
		stock.setWoId(receiveLog.getWoId());

		return stock;
	}

	public Stock create(Stock sourceStock, Location targetLocation,
						String targetLpnCode, String targetBoxCode, BigDecimal qty){
		AssertUtil.notNull(sourceStock, "创建库存失败，原库存为空");
		AssertUtil.notNull(targetLocation, "创建库存失败，目标库位为空");
		AssertUtil.notNull(qty, "创建库存失败，数量为空");

		Stock targetStock = new Stock();
		BeanUtil.copy(sourceStock, targetStock);
		StockUtil.resetStockInfo(targetStock);

		targetStock.setStockQty(qty);
		targetStock.setLocId(targetLocation.getLocId());
		targetStock.setLocCode(targetLocation.getLocCode());
		targetStock.setZoneId(targetLocation.getZoneId());
		targetStock.setLpnCode(targetLpnCode);
		targetStock.setBoxCode(targetBoxCode);
		Zone zone = zoneBiz.findById(targetLocation.getZoneId());
		targetStock.setZoneCode(zone.getZoneCode());
		targetStock.setLastInTime(LocalDateTime.now());

		return targetStock;
	}
}
