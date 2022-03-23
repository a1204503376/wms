
package org.nodes.wms.core.basedata.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.core.basedata.entity.SkuOutstock;
import org.nodes.wms.core.basedata.service.ISkuPackageService;
import org.nodes.wms.core.basedata.vo.SkuOutstockVO;
import org.nodes.wms.core.strategy.cache.OutstockCache;
import org.nodes.wms.core.strategy.entity.Outstock;
import org.nodes.wms.core.strategy.service.IOutstockService;
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
 * 物品出库设置包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2019-12-23
 */
public class SkuOutstockWrapper extends BaseEntityWrapper<SkuOutstock, SkuOutstockVO> {

	public static SkuOutstockWrapper build() {
		return new SkuOutstockWrapper();
	}

	@Override
	public SkuOutstockVO entityVO(SkuOutstock skuOutstock) {
		IOutstockService outstockService = SpringUtil.getBean(IOutstockService.class);
		SkuOutstockVO skuOutstockVO = BeanUtil.copy(skuOutstock, SkuOutstockVO.class);
		if (ObjectUtil.isNotEmpty(skuOutstockVO)) {
			// 库房名称
			Warehouse warehouse = WarehouseCache.getById(skuOutstock.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				skuOutstockVO.setWhName(warehouse.getWhName());
			}
			// 策略名称

			Outstock outstock = outstockService.getById(skuOutstock.getSsoId());
			if (Func.isNotEmpty(outstock)) {
				skuOutstockVO.setSsoName(outstock.getSsoName());
			}
			// 设置包装名称
			SkuPackage skuPackage = SkuPackageCache.getById(skuOutstockVO.getWspId());
			if (ObjectUtil.isNotEmpty(skuPackage)) {

				skuOutstockVO.setWspName(skuPackage.getWspName());

				if (ObjectUtil.isNotEmpty(skuPackage.getWspId()) &&
					ObjectUtil.isNotEmpty(skuOutstockVO.getSkuLevel())) {

					SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(
						skuPackage.getWspId(), skuOutstockVO.getSkuLevel());
					if (ObjectUtil.isNotEmpty(skuPackageDetail)) {
						skuOutstockVO.setWspdId(skuPackageDetail.getWspdId());
					}
				}
			}
			// 质检库区
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone qcZone = zoneService.getById(skuOutstock.getQcZoneId());
			if (ObjectUtil.isNotEmpty(qcZone)) {
				skuOutstockVO.setQcZoneName(qcZone.getZoneName());
			}
			// 质检库位
			Location qcLocation = LocationCache.getById(skuOutstock.getQcLocId());
			if (ObjectUtil.isNotEmpty(qcLocation)) {
				skuOutstockVO.setQcLocCode(qcLocation.getLocCode());
			}
			// 不合格库区
			Zone unqualifiyZone = zoneService.getById(skuOutstock.getUnqualiflyZoneId());
			if (ObjectUtil.isNotEmpty(unqualifiyZone)) {
				skuOutstockVO.setUnqualifiyZoneName(unqualifiyZone.getZoneName());
			}
			// 不合格库位
			Location unqualifiyLocation = LocationCache.getById(skuOutstock.getUnqualiflyLocId());
			if (ObjectUtil.isNotEmpty(unqualifiyLocation)) {
				skuOutstockVO.setUnqualifiyLocCode(unqualifiyLocation.getLocCode());
			}
			Zone firstSoZone = zoneService.getById(skuOutstock.getFirstSoZoneId());
			if (ObjectUtil.isNotEmpty(firstSoZone)) {
				skuOutstockVO.setFirstSoZoneName(firstSoZone.getZoneName());
			}
			// 默认出库层级
			skuOutstockVO.setSkuLevelDesc(DictCache.getValue(DictConstant.SKU_LEVEL, skuOutstock.getSkuLevel()));
			// 默认出库库位
			ILocationService locationService2 = SpringUtil.getBean(ILocationService.class);
			Location location = locationService2.getById(skuOutstock.getLocId());
			if (ObjectUtil.isNotEmpty(location)) {
				skuOutstockVO.setLocCode(location.getLocCode());
			}
			// 周转方式
			skuOutstockVO.setTurnoverType1Desc(
				DictCache.getValue(DictConstant.TURNOVER_TYPE, skuOutstock.getTurnoverType1())
			);
			skuOutstockVO.setTurnoverType2Desc(
				DictCache.getValue(DictConstant.TURNOVER_TYPE, skuOutstock.getTurnoverType2())
			);
			skuOutstockVO.setTurnoverType3Desc(
				DictCache.getValue(DictConstant.TURNOVER_TYPE, skuOutstock.getTurnoverType3())
			);
			// 周转类型
			skuOutstockVO.setTurnoverItem1Desc(
				DictCache.getValue(DictConstant.TURNOVER_ITEM, skuOutstock.getTurnoverItem1())
			);
			skuOutstockVO.setTurnoverItem2Desc(
				DictCache.getValue(DictConstant.TURNOVER_ITEM, skuOutstock.getTurnoverItem2())
			);
			skuOutstockVO.setTurnoverItem3Desc(
				DictCache.getValue(DictConstant.TURNOVER_ITEM, skuOutstock.getTurnoverItem3())
			);
		}
		return skuOutstockVO;
	}

}
