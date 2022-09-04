package org.nodes.wms.core.strategy.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.dao.putaway.entities.StInstockDetail;
import org.nodes.wms.core.strategy.vo.InstockDetailVO;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;

public class InstockDetailWrapper extends BaseEntityWrapper<StInstockDetail, InstockDetailVO> {

	public static final String COMBINE_CONDITION = "combine_condition";

	private static final String ONE = StringPool.ONE;
	private static final String ALLOW = "允许混合";
	private static final String UN_ALLOW = "不允许混合";

	public static InstockDetailWrapper build() {
		return new InstockDetailWrapper();
	}

	@Override
	public InstockDetailVO entityVO(StInstockDetail entity) {
		InstockDetailVO instockDetail = BeanUtil.copy(entity, InstockDetailVO.class);
		if (ObjectUtil.isNotEmpty(instockDetail)) {
			instockDetail.setInstockFunctionDesc(
				DictCache.getValue(DictCodeConstant.INSTOCK_FUNCTION, instockDetail.getInstockFunction()));
			// 从库区类型
			instockDetail.setFromZoneTypeName(
				DictCache.getValue(DictCodeConstant.ZONE_TYPE, instockDetail.getFromZoneType()));
			// 从库区
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone fromZone = zoneService.getById(entity.getFromZoneId());
			if (ObjectUtil.isNotEmpty(fromZone)) {
				instockDetail.setFromZoneName(fromZone.getZoneName());
			}
			// 从库位
			Location fromLoc = LocationCache.getById(entity.getFromLocId());
			if (ObjectUtil.isNotEmpty(fromLoc)) {
				instockDetail.setFromLocCode(fromLoc.getLocCode());
			}
			// 从库区类型
			instockDetail.setToZoneTypeName(
				DictCache.getValue(DictCodeConstant.ZONE_TYPE, instockDetail.getToZoneType()));
			// 至库区
			Zone toZone = zoneService.getById(entity.getToZoneId());
			if (ObjectUtil.isNotEmpty(toZone)) {
				instockDetail.setToZoneName(toZone.getZoneName());
			}
			// 至库位
			Location toLoc = LocationCache.getById(entity.getToLocId());
			if (ObjectUtil.isNotEmpty(toLoc)) {
				instockDetail.setToLocCode(toLoc.getLocCode());
			}
			// 合并条件
			instockDetail.setConfMixWightDesc(DictCache.getValue(COMBINE_CONDITION, entity.getConfMixWight()));
			instockDetail.setConfMixWightBool(entity.getConfMixWight() == 0 ? false : true);

			instockDetail.setConfMixVolumeDesc(DictCache.getValue(COMBINE_CONDITION, entity.getConfMixVolume()));
			instockDetail.setConfMixVolumeBool(entity.getConfMixVolume() == 0 ? false : true);

			instockDetail.setConfMixBoxCountDesc(DictCache.getValue(COMBINE_CONDITION, entity.getConfMixBoxCount()));
			instockDetail.setConfMixBoxCountBool(entity.getConfMixBoxCount() == 0 ? false : true);
			// 物品是否允许混放
			instockDetail.setSkuMixDesc(entity.getSkuMix() == 0 ? StringPool.CHS_NO : StringPool.CHS_NO);
			instockDetail.setSkuMixBool(entity.getSkuMix() == 0 ? false : true);
			// 混合存放
			instockDetail.setSkuLotMix1Desc(ONE.equals(instockDetail.getSkuLotMix1()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix2Desc(ONE.equals(instockDetail.getSkuLotMix2()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix3Desc(ONE.equals(instockDetail.getSkuLotMix3()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix4Desc(ONE.equals(instockDetail.getSkuLotMix4()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix5Desc(ONE.equals(instockDetail.getSkuLotMix5()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix6Desc(ONE.equals(instockDetail.getSkuLotMix6()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix7Desc(ONE.equals(instockDetail.getSkuLotMix7()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix8Desc(ONE.equals(instockDetail.getSkuLotMix8()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix9Desc(ONE.equals(instockDetail.getSkuLotMix9()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix10Desc(ONE.equals(instockDetail.getSkuLotMix10()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix11Desc(ONE.equals(instockDetail.getSkuLotMix11()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix12Desc(ONE.equals(instockDetail.getSkuLotMix12()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix13Desc(ONE.equals(instockDetail.getSkuLotMix13()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix14Desc(ONE.equals(instockDetail.getSkuLotMix14()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix15Desc(ONE.equals(instockDetail.getSkuLotMix15()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix16Desc(ONE.equals(instockDetail.getSkuLotMix16()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix17Desc(ONE.equals(instockDetail.getSkuLotMix17()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix18Desc(ONE.equals(instockDetail.getSkuLotMix18()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix19Desc(ONE.equals(instockDetail.getSkuLotMix19()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix20Desc(ONE.equals(instockDetail.getSkuLotMix20()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix21Desc(ONE.equals(instockDetail.getSkuLotMix21()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix22Desc(ONE.equals(instockDetail.getSkuLotMix22()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix23Desc(ONE.equals(instockDetail.getSkuLotMix23()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix24Desc(ONE.equals(instockDetail.getSkuLotMix24()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix25Desc(ONE.equals(instockDetail.getSkuLotMix25()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix26Desc(ONE.equals(instockDetail.getSkuLotMix26()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix27Desc(ONE.equals(instockDetail.getSkuLotMix27()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix28Desc(ONE.equals(instockDetail.getSkuLotMix28()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix29Desc(ONE.equals(instockDetail.getSkuLotMix29()) ? ALLOW : UN_ALLOW);
			instockDetail.setSkuLotMix30Desc(ONE.equals(instockDetail.getSkuLotMix30()) ? ALLOW : UN_ALLOW);
		}
		return instockDetail;
	}
}
