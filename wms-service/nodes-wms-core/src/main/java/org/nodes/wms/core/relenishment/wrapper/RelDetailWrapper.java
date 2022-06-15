
package org.nodes.wms.core.relenishment.wrapper;

import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.relenishment.entity.RelDetail;
import org.nodes.wms.core.relenishment.enums.RelStateEnum;
import org.nodes.wms.core.relenishment.vo.RelDetailVo;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;


/**
 * 库存包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2019-12-24
 */
public class RelDetailWrapper extends BaseEntityWrapper<RelDetail, RelDetailVo> {

	public static RelDetailWrapper build() {
		return new RelDetailWrapper();
	}

	@Override
	public RelDetailVo entityVO(RelDetail relDetail) {
		RelDetailVo relDetailVo = BeanUtil.copy(relDetail, RelDetailVo.class);
		if (Func.isNotEmpty(relDetailVo)) {
			switch (relDetailVo.getRelStatus()){
				case 10:
					relDetailVo.setRelBillStateDesc(RelStateEnum.UNEXECUTED.getName());
					break;
				case 20:
					relDetailVo.setRelBillStateDesc(RelStateEnum.EXECUTING.getName());
					break;
				case 30:
					relDetailVo.setRelBillStateDesc(RelStateEnum.EXECUTED.getName());
					break;
			}
			Warehouse warehouse = WarehouseCache.getById(relDetailVo.getFromWhId());
			if (ObjectUtil.isNotEmpty(warehouse)) {
				relDetailVo.setFromWhName(warehouse.getWhName());
			}
			Warehouse warehouse1 = WarehouseCache.getById(relDetailVo.getToWhId());
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			if (ObjectUtil.isNotEmpty(warehouse1)) {
				relDetailVo.setToWhName(warehouse1.getWhName());
			}
			Zone zone = zoneService.getById(relDetailVo.getFromZoneId());
			if (ObjectUtil.isNotEmpty(zone)) {
				relDetailVo.setFromZoneName(zone.getZoneName());
			}
			Zone zone1 = zoneService.getById(relDetailVo.getToZoneId());
			if (ObjectUtil.isNotEmpty(zone1)) {
				relDetailVo.setToZoneName(zone1.getZoneName());
			}
			Location location = LocationCache.getById(relDetailVo.getFromLocId());
			if (ObjectUtil.isNotEmpty(location)) {
				relDetailVo.setFromLocName(location.getLocCode());
			}
			Location location1 = LocationCache.getById(relDetailVo.getToLocId());
			if (ObjectUtil.isNotEmpty(location1)) {
				relDetailVo.setToLocName(location1.getLocCode());
			}
			Sku sku = SkuCache.getById(relDetail.getSkuId());
			Long skuWspId = sku.getWspId();
			SkuPackage skuPackage = SkuPackageCache.getById(skuWspId);
			relDetailVo.setSkuSpec(skuPackage.getSpec());
		}
		return relDetailVo;
	}
}
