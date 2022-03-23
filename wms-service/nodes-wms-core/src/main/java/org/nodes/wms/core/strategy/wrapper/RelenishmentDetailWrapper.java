package org.nodes.wms.core.strategy.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.strategy.entity.RelenishmentDetail;
import org.nodes.wms.core.strategy.vo.RelenishmentDetailVO;
import org.nodes.wms.core.warehouse.cache.ZoneCache;
import org.nodes.wms.core.warehouse.entity.Zone;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;

public class RelenishmentDetailWrapper extends BaseEntityWrapper<RelenishmentDetail, RelenishmentDetailVO> {

	public static final String COMBINE_CONDITION = "combine_condition";

	private static final String ONE = StringPool.ONE;

	public static RelenishmentDetailWrapper build() {
		return new RelenishmentDetailWrapper();
	}

	@Override
	public RelenishmentDetailVO entityVO(RelenishmentDetail entity) {
		RelenishmentDetailVO relenishmentDetail = BeanUtil.copy(entity, RelenishmentDetailVO.class);
		if (ObjectUtil.isNotEmpty(relenishmentDetail)) {
//			if (Func.isNotEmpty(relenishmentDetail.getInstockFunction())) {
//				relenishmentDetail.setInstockFunctionDesc(
//					DictCache.getValue(DictConstant.INSTOCK_FUNCTION, relenishmentDetail.getInstockFunction()));
//			}
			// 从库区
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone fromZone = zoneService.getById(entity.getFromZoneId());
			if (ObjectUtil.isNotEmpty(fromZone)) {
				relenishmentDetail.setFromZoneName(fromZone.getZoneName());
			}
			relenishmentDetail.setSkuLevelDesc(DictCache.getValue(DictConstant.SKU_LEVEL,entity.getSkuLevel()));
			// 从库位
//			Location fromLoc = LocationCache.getById(entity.getFromLocId());
//			if (ObjectUtil.isNotEmpty(fromLoc)) {
//				relenishmentDetail.setFromLocCode(fromLoc.getLocCode());
//			}
			// 至库区
			Zone toZone = zoneService.getById(entity.getToZoneId());
			if (ObjectUtil.isNotEmpty(toZone)) {
				relenishmentDetail.setToZoneName(toZone.getZoneName());
			}
			// 至库位
//			Location toLoc = LocationCache.getById(entity.getToLocId());
//			if (ObjectUtil.isNotEmpty(toLoc)) {
//				relenishmentDetail.setToLocCode(toLoc.getLocCode());
//			}
		}
		return relenishmentDetail;
	}
}
