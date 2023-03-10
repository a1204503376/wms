package org.nodes.wms.core.basedata.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.service.ISkuTypeService;
import org.nodes.wms.core.basedata.vo.SkuPackageVO;
import org.nodes.wms.core.basedata.vo.SkuVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * @author pengwei
 * @program WmsCore
 * @description Sku封装类
 * @create 20191210
 */
public class SkuWrapper extends BaseEntityWrapper<Sku, SkuVO> {

	public static SkuWrapper build() {
		return new SkuWrapper();
	}

	@Override
	public SkuVO entityVO(Sku entity) {
		SkuVO skuVO = BeanUtil.copy(entity, SkuVO.class);
		if (Func.isNotEmpty(skuVO)) {

			SkuPackageVO skuPackage = SkuPackageWrapper.build().entityVO(SkuPackageCache.getById(skuVO.getWspId()));
			if (Func.isNotEmpty(skuPackage)) {

				skuVO.setSkuPackage(skuPackage);
				skuVO.setWspName(skuPackage.getWspName());
				skuVO.setSpec(skuPackage.getSpec());
			}
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.getById(skuVO.getWoId());
			if (Func.isNotEmpty(owner)) {
				//货主名称
				skuVO.setOwnerName(owner.getOwnerName());
			}
			ISkuTypeService skuTypeService = SpringUtil.getBean(ISkuTypeService.class);
			SkuType skuType = skuTypeService.getById(skuVO.getSkuTypeId());
			if (Func.isNotEmpty(skuType)) {
				//货品分类名称
				skuVO.setTypeName(skuType.getTypeName());
			}
			if (Func.isNotEmpty(skuVO.getIsSn())) {
				skuVO.setIsSnDesc(skuVO.getIsSn() != 0 ? StringPool.CHS_YES : StringPool.CHS_NO);
				skuVO.setSn(skuVO.getIsSn() != 0 ? true : false);
			}
			skuVO.setQualityDateTypeCd(DictCache.getValue(DictCodeConstant.QUALITY_TYPE, skuVO.getQualityDateType()));
			skuVO.setInventoryTypeDesc(DictCache.getValue(DictCodeConstant.INVENTORY_TYPE, skuVO.getSkuStorageType()));
		}
		return skuVO;
	}
}
