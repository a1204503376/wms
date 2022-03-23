
package org.nodes.wms.core.basedata.wrapper;

import com.sun.xml.bind.v2.model.core.ID;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.basedata.entity.SkuInstock;
import org.nodes.wms.core.basedata.vo.SkuInstockVO;
import org.nodes.wms.core.strategy.cache.InstockCache;
import org.nodes.wms.core.strategy.entity.Instock;
import org.nodes.wms.core.strategy.service.IInstockService;
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
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * 物品入库设置包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2019-12-23
 */
public class SkuInstockWrapper extends BaseEntityWrapper<SkuInstock, SkuInstockVO> {

	public static SkuInstockWrapper build() {
		return new SkuInstockWrapper();
	}

	@Override
	public SkuInstockVO entityVO(SkuInstock skuInstock) {
		SkuInstockVO skuInstockVO = BeanUtil.copy(skuInstock, SkuInstockVO.class);
		if (ObjectUtil.isNotEmpty(skuInstockVO)) {
			// 库房
			Warehouse warehouse = WarehouseCache.getById(skuInstock.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				skuInstockVO.setWhName(warehouse.getWhName());
			}
			// 上架策略
			IInstockService instockService = SpringUtil.getBean(IInstockService.class);
			Instock instock = instockService.getById(skuInstock.getSsiId());
			if (Func.isNotEmpty(instock)) {
				skuInstockVO.setSsiName(instock.getSsiName());
			}
			// 上架策略执行类型
			skuInstockVO.setSsiProcTypeName(
				DictCache.getValue(DictConstant.SSI_PROC_TYPE, skuInstock.getSsiProcType())
			);
			// 上架库区
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone zone = zoneService.getById(skuInstockVO.getZoneId());
			if (ObjectUtil.isNotEmpty(zone)) {
				skuInstockVO.setZoneName(zone.getZoneName());
			}
			// 上架库位
			Location location = LocationCache.getById(skuInstock.getLocId());
			if (ObjectUtil.isNotEmpty(location)) {
				skuInstockVO.setLocCode(location.getLocCode());
			}
			// 质检库区
			Zone qcZone = zoneService.getById(skuInstockVO.getQcZoneId());
			if (ObjectUtil.isNotEmpty(qcZone)) {
				skuInstockVO.setQcZoneName(qcZone.getZoneName());
			}
			// 质检库位
			Location qcLocation = LocationCache.getById(skuInstock.getQcLocId());
			if (ObjectUtil.isNotEmpty(qcLocation)) {
				skuInstockVO.setQcLocCode(qcLocation.getLocCode());
			}
			// 不合格品库区
			Zone unqualifiyZone = zoneService.getById(skuInstockVO.getUnqualiflyZoneId());
			if (ObjectUtil.isNotEmpty(unqualifiyZone)) {
				skuInstockVO.setUnqualifiyZoneName(unqualifiyZone.getZoneName());
			}
			// 不合格品库位
			Location unqualifiyLocation = LocationCache.getById(skuInstock.getUnqualiflyLocId());
			if (ObjectUtil.isNotEmpty(unqualifiyLocation)) {
				skuInstockVO.setUnqualifiyLocCode(unqualifiyLocation.getLocCode());
			}
			// 退货库区
			Zone returnZone = zoneService.getById(skuInstockVO.getReturnZoneId());
			if (ObjectUtil.isNotEmpty(returnZone)) {
				skuInstockVO.setReturnZoneName(returnZone.getZoneName());
			}
			// 退货库位
			Location returnLocation = LocationCache.getById(skuInstock.getReturnLocId());
			if (ObjectUtil.isNotEmpty(returnLocation)) {
				skuInstockVO.setReturnLocCode(returnLocation.getLocCode());
			}
			// 拣货库区
			Zone pickZone = zoneService.getById(skuInstockVO.getPickZoneId());
			if (ObjectUtil.isNotEmpty(pickZone)) {
				skuInstockVO.setPickZoneName(pickZone.getZoneName());
			}
			Location pickLocation = LocationCache.getById(skuInstock.getPickLocId());
			if (ObjectUtil.isNotEmpty(pickLocation)) {
				skuInstockVO.setPickLocCode(pickLocation.getLocCode());
			}
		}
		return skuInstockVO;
	}

}
