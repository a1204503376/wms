package org.nodes.wms.core.basedata.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.basedata.vo.SkuPackageDetailVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

/**
 * @author pengwei
 * @program WmsCore
 * @description SkuPackageDetail 封装类
 * @create 20191211
 */
public class SkuPackageDetailWrapper extends BaseEntityWrapper<SkuPackageDetail, SkuPackageDetailVO> {

	public static SkuPackageDetailWrapper build(){
		return new SkuPackageDetailWrapper();
	}

	@Override
	public SkuPackageDetailVO entityVO(SkuPackageDetail entity) {
		SkuPackageDetailVO skuPackageDetailVO = BeanUtil.copy(entity, SkuPackageDetailVO.class);
		if (Func.isNotEmpty(skuPackageDetailVO)) {
			skuPackageDetailVO.setSkuLevelName(DictCache.getValue(DictConstant.SKU_LEVEL, skuPackageDetailVO.getSkuLevel()));
		}
		return skuPackageDetailVO;
	}
}
