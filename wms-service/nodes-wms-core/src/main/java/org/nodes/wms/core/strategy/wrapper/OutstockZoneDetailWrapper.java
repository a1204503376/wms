
package org.nodes.wms.core.strategy.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.strategy.entity.OutstockZoneDetail;
import org.nodes.wms.core.strategy.vo.OutstockZoneDetailVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.cache.ZoneCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.entity.Zone;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;

/**
 * 分配策略货源库区包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2020-02-12
 */
public class OutstockZoneDetailWrapper extends BaseEntityWrapper<OutstockZoneDetail, OutstockZoneDetailVO> {

	public static OutstockZoneDetailWrapper build() {
		return new OutstockZoneDetailWrapper();
	}

	@Override
	public OutstockZoneDetailVO entityVO(OutstockZoneDetail outstockZoneDetail) {
		OutstockZoneDetailVO outstockZoneDetailVO = BeanUtil.copy(outstockZoneDetail, OutstockZoneDetailVO.class);

		Warehouse warehouse = WarehouseCache.getById(outstockZoneDetail.getWhId());
		if (ObjectUtil.isNotEmpty(warehouse)) {
			outstockZoneDetailVO.setWhName(warehouse.getWhName());
		}

		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		Zone zone = zoneService.getById(outstockZoneDetailVO.getZoneId());
		if (ObjectUtil.isNotEmpty(zone)) {
			outstockZoneDetailVO.setZoneType(zone.getZoneType());
			outstockZoneDetailVO.setZoneName(
				zone.getZoneName() + "（" + DictCache.getValue(DictConstant.ZONE_TYPE, zone.getZoneType()) + "）");
		}
		outstockZoneDetailVO.setSkuLevelDesc(DictCache.getValue(DictConstant.SKU_LEVEL, outstockZoneDetail.getSkuLevel()));

		return outstockZoneDetailVO;
	}
}
