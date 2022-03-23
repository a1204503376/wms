package org.nodes.wms.core.outstock.loading.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.outstock.loading.entity.SoTruckHeader;
import org.nodes.wms.core.outstock.loading.entity.TruckStock;
import org.nodes.wms.core.outstock.loading.service.ISoTruckHeaderService;
import org.nodes.wms.core.outstock.loading.vo.TruckStockVO;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.cache.ZoneCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.entity.Zone;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;


/**
 * 包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2020-04-16
 */
public class TruckStockWrapper extends BaseEntityWrapper<TruckStock, TruckStockVO> {

	private static ISoTruckHeaderService soTruckHeaderService;

	static {
		soTruckHeaderService = SpringUtil.getBean(ISoTruckHeaderService.class);
	}

	public static TruckStockWrapper build() {
		return new TruckStockWrapper();
	}

	@Override
	public TruckStockVO entityVO(TruckStock truckStock) {
		TruckStockVO truckStockVO = BeanUtil.copy(truckStock, TruckStockVO.class);
		if (ObjectUtil.isNotEmpty(truckStock)) {
			SoTruckHeader soTruckHeader = soTruckHeaderService.getById(truckStock.getTruckId());
			if (ObjectUtil.isNotEmpty(soTruckHeader)) {
				truckStockVO.setTruckStateDesc(DictCache.getValue("truck_state", soTruckHeader.getTruckState()));
				truckStockVO.setPlateNumber(soTruckHeader.getPlateNumber());
				truckStockVO.setDriverName(soTruckHeader.getDriverName());
				truckStockVO.setDriverPhone(soTruckHeader.getDriverPhone());
			}

			Warehouse warehouse = WarehouseCache.getById(truckStock.getWhId());
			if (ObjectUtil.isNotEmpty(warehouse)) {
				truckStockVO.setWhName(warehouse.getWhName());
			}

			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone zone = zoneService.getById(truckStock.getZoneId());
			if (ObjectUtil.isNotEmpty(zone)) {
				truckStockVO.setZoneName(zone.getZoneName());
			}

			Location location = LocationCache.getById(truckStock.getLocId());
			if (ObjectUtil.isNotEmpty(location)) {
				truckStockVO.setLocCode(location.getLocCode());
			}

			Sku sku = SkuCache.getById(truckStock.getSkuId());
			if (ObjectUtil.isNotEmpty(sku)) {
				truckStockVO.setSkuCode(sku.getSkuCode());
				truckStockVO.setSkuName(sku.getSkuName());
			}
			SkuPackageDetail packageDetail = SkuPackageDetailCache.getOne(truckStock.getWspId(), truckStock.getSkuLevel());
			if (ObjectUtil.isNotEmpty(packageDetail)) {
				truckStockVO.setUmName(packageDetail.getWsuName());
			}
		}

		return truckStockVO;
	}
}
