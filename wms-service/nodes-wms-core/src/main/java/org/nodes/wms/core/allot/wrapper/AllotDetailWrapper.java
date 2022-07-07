package org.nodes.wms.core.allot.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.wms.core.allot.entity.AllotDetail;
import org.nodes.wms.core.allot.vo.AllotDetailVO;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

import java.math.BigDecimal;

/**
 * 调拨单明细表 封装类
 *
 * @Author wangjw
 * @Date 2020/02/24
 **/
public class AllotDetailWrapper extends BaseEntityWrapper<AllotDetail, AllotDetailVO> {
	public static AllotDetailWrapper build() {
		return new AllotDetailWrapper();
	}

	@Override
	public AllotDetailVO entityVO(AllotDetail entity) {
		AllotDetailVO allotDetailVO = BeanUtil.copy(entity, AllotDetailVO.class);
		//包装名称
		SkuPackage skuPackage = SkuPackageCache.getById(entity.getWspId());
		if (Func.isNotEmpty(skuPackage)) {
			allotDetailVO.setWspName(skuPackage.getWspName());
		}
		String skuLevelName = DictCache.getValue(DictCodeConstant.SKU_LEVEL, entity.getSkuLevel());
		allotDetailVO.setSkuLevelName(skuLevelName);
		//计划数量
		if (Func.isNotEmpty(allotDetailVO.getWspId())
			&& Func.isNotEmpty(allotDetailVO.getSkuLevel())
			&& Func.isNotEmpty(allotDetailVO.getAllotPlanQty())) {
			allotDetailVO.setAllotPlanQtyName(SkuPackageDetailCache.convert(allotDetailVO.getWspId(),
				allotDetailVO.getSkuLevel(), allotDetailVO.getAllotPlanQty()));
		}
		if (Func.isNotEmpty(allotDetailVO.getAllotPlanQty()) && Func.isNotEmpty(allotDetailVO.getConvertQty()))
			allotDetailVO.setAllotPlanQty(allotDetailVO.getAllotPlanQty()
				.divide(new BigDecimal(allotDetailVO.getConvertQty()), 6, BigDecimal.ROUND_CEILING));
		return allotDetailVO;
	}
}
