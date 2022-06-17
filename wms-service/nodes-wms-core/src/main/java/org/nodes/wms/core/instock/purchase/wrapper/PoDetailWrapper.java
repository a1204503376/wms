package org.nodes.wms.core.instock.purchase.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.basedata.service.ISkuTypeService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.nodes.wms.core.instock.purchase.entity.PoDetail;
import org.nodes.wms.core.instock.purchase.vo.PoDetailVO;


import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;

import java.math.BigDecimal;


/**
 * 收货单明细表包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-05-31
 */
public class PoDetailWrapper extends BaseEntityWrapper<PoDetail, PoDetailVO> {

	public static PoDetailWrapper build() {
		return new PoDetailWrapper();
	}

	@Override
	public PoDetailVO entityVO(PoDetail poDetail) {
		PoDetailVO poDetailVO = BeanUtil.copy(poDetail, PoDetailVO.class);
		if (Func.isNotEmpty(poDetailVO)) {
			//接收状态
			if (Func.isNotEmpty(poDetailVO.getDetailStatus())) {
				poDetailVO.setDetailStatusName(DictCache.getValue(DictConstant.DETAIL_STATUS, poDetailVO.getDetailStatus()));
			}
			//包装名称
			SkuPackage skuPackage = SkuPackageCache.getById(poDetailVO.getWspId());
			if (Func.isNotEmpty(skuPackage)) {
				poDetailVO.setWspName(skuPackage.getWspName());
			}
			//是否序列号
			Sku sku = SkuCache.getById(poDetailVO.getSkuId());
			if (Func.isNotEmpty(sku)) {
				if (Func.isNotEmpty(sku.getIsSn())) {
					poDetailVO.setIsSn(DictCache.getValue(DictConstant.IS_SN, sku.getIsSn()));
				} else {
					poDetailVO.setIsSn(StringPool.CHS_NO);
				}
				ISkuTypeService skuTypeService = SpringUtil.getBean(ISkuTypeService.class);
				SkuType skuType = skuTypeService.getById(sku.getSkuTypeId());
				if (Func.isNotEmpty(skuType)) {
					poDetailVO.setSkuType(skuType.getTypeName());
				}
			}
			if (Func.isNotEmpty(poDetailVO.getImportSn())) {
				poDetailVO.setImportSnName(StringPool.Y.toUpperCase().equals(poDetailVO.getImportSn()) ? "已导入" : "未导入");
			} else {
				poDetailVO.setImportSnName("未导入");
			}
			//层级
			poDetailVO.setSkuLevelName(DictCache.getValue(DictConstant.SKU_LEVEL, poDetailVO.getSkuLevel()));
			//计划数量
			poDetailVO.setPlanQtyName(SkuPackageDetailCache.convert(poDetailVO.getWspId(),
				poDetailVO.getSkuLevel(), poDetailVO.getPlanQty()));
			//实际数量
			poDetailVO.setScanQtyName(SkuPackageDetailCache.convert(poDetailVO.getWspId(),
				poDetailVO.getSkuLevel(), poDetailVO.getScanQty()));
			//剩余数量
			poDetailVO.setSurplusQtyName(SkuPackageDetailCache.convert(poDetailVO.getWspId(),
				poDetailVO.getSkuLevel(), poDetailVO.getSurplusQty()));

			//包装明细ID
			if (Func.isNotEmpty(poDetailVO.getSkuLevel())) {
				SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(
					poDetailVO.getWspId(), poDetailVO.getSkuLevel());
				if (ObjectUtil.isNotEmpty(skuPackageDetail)) {
					poDetailVO.setWspdId(skuPackageDetail.getWspdId());
				}
			}
			//计划数量
			poDetailVO.setPlanQty(poDetailVO.getPlanQty().divide(new BigDecimal(poDetailVO.getConvertQty())));
		}
		return poDetailVO;
	}
}
