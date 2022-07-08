package org.nodes.wms.core.stock.core.wrapper;

import net.sf.cglib.beans.BeanCopier;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.entity.StockSnapshoot;
import org.nodes.wms.core.stock.core.vo.StockSnapshootVO;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * 库存快照包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-05-28
 */
public class StockSnapshootWrapper extends BaseEntityWrapper<StockSnapshoot, StockSnapshootVO> {

	public static StockSnapshootWrapper build() {
		return new StockSnapshootWrapper();
	}

	@Override
	public StockSnapshootVO entityVO(StockSnapshoot stockSnapshoot) {
		StockSnapshootVO stockSnapshootVO = BeanUtil.copy(stockSnapshoot, StockSnapshootVO.class);
		if (Func.isNotEmpty(stockSnapshootVO)) {
			stockSnapshootVO.setSkuLevelName(DictCache.getValue(DictCodeConstant.SKU_LEVEL, stockSnapshootVO.getSkuLevel()));
		}
		return stockSnapshootVO;
	}

	public StockSnapshoot entity(Stock stock) {

		StockSnapshoot stockSnapshoot = new StockSnapshoot();
		BeanCopier copier = BeanCopier.create(Stock.class, StockSnapshoot.class, false);
		copier.copy(stock, stockSnapshoot, null);
		stockSnapshoot.setSnapshootDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		//库房信息
		if (Func.isNotEmpty(stock)) {
			Warehouse warehouse = WarehouseCache.getById(stock.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				stockSnapshoot.setWhId(stock.getWhId());
				stockSnapshoot.setWhCode(warehouse.getWhCode());
				stockSnapshoot.setWhName(warehouse.getWhName());
			}
			//库区信息
			if (Func.isNotEmpty(stock.getZoneId())) {
				IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
				Zone zone = zoneService.getById(stock.getZoneId());
				if (Func.isNotEmpty(zone)) {
					stockSnapshoot.setZoneId(stock.getZoneId());
					stockSnapshoot.setZoneCode(zone.getZoneCode());
					stockSnapshoot.setZoneName(zone.getZoneName());
				}
			}
			//库位信息
			Location location = LocationCache.getById(stock.getLocId());
			if (Func.isNotEmpty(location)) {
				stockSnapshoot.setLocId(stock.getLocId());
				stockSnapshoot.setLocCode(location.getLocCode());
			}
			//物品信息
			if (Func.isNotEmpty(stock.getSkuId())) {
				Sku sku = SkuCache.getById(stock.getSkuId());
				if (Func.isNotEmpty(sku)) {
					stockSnapshoot.setSkuId(stock.getSkuId());
					stockSnapshoot.setSkuCode(sku.getSkuCode());
					stockSnapshoot.setSkuName(sku.getSkuName());
				}
			}
			//包装信息
			SkuPackage skuPackage = SkuPackageCache.getById(stock.getWspId());
			if (Func.isNotEmpty(skuPackage)) {
				stockSnapshoot.setWspId(stock.getWspId());
				stockSnapshoot.setWspName(skuPackage.getWspName());
			}
			//货主
			if (Func.isNotEmpty(stock.getWoId())) {
				IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
				Owner owner = ownerService.getById(stock.getWoId());
				if (Func.isNotEmpty(owner)) {
					stockSnapshoot.setWoId(stock.getWoId());
					stockSnapshoot.setOwnerCode(owner.getOwnerCode());
					stockSnapshoot.setOwnerName(owner.getOwnerName());
				}
			}
//			//容器
//			if (Func.isNotEmpty(stock.getLpnCode())) {
//				stockSnapshoot.setLpnCode(stock.getLpnCode());
//			}
//			//箱号
//			if (Func.isNotEmpty(stock.getBoxCode())) {
//				stockSnapshoot.setBoxCode(stock.getBoxCode());
//			}
//			//批次
//			if (Func.isNotEmpty(stock.getLotNumber())) {
//				stockSnapshoot.setLotNumber(stock.getLotNumber());
//			}
			//订单明细
//			if (Func.isNotEmpty(stock.getBillDetailId())) {
//				stockSnapshoot.setBillDetailId(stock.getBillDetailId());
//			}
//			//波次
//			if (Func.isNotEmpty(stock.getWellenId())) {
//				stockSnapshoot.setWellenId(stock.getWellenId());
//			}
			//库存状态
//			if (Func.isNotEmpty(stock.getStockStatus())) {
//				stockSnapshoot.setStockStatus(stock.getStockStatus());
//			}
//			if (Func.isNotEmpty(stock.getLastInTime())) {
//				stockSnapshoot.setLastInTime(stock.getLastInTime());
//			}
//			if (Func.isNotEmpty(stock.getLastOutTime())) {
//				stockSnapshoot.setLastOutTime(stock.getLastOutTime());
//			}

//			if (Func.isNotEmpty(stock.getSkuLot1())) {
//				stockSnapshoot.setSkuLot1(stock.getSkuLot1());
//			}


		}

		return stockSnapshoot;

	}
}
