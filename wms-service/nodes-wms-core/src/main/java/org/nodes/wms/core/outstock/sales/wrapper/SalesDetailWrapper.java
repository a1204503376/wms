package org.nodes.wms.core.outstock.sales.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictCodeConstant;
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
import org.nodes.wms.core.outstock.sales.entity.SalesDetail;
import org.nodes.wms.core.outstock.sales.vo.SalesDetailVO;


import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;

import java.math.BigDecimal;


/**
 * 发货单明细
 * 包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-05-31
 */
public class SalesDetailWrapper extends BaseEntityWrapper<SalesDetail, SalesDetailVO> {

	public static SalesDetailWrapper build() {
		return new SalesDetailWrapper();
	}

	@Override
	public SalesDetailVO entityVO(SalesDetail salesDetail) {
		SalesDetailVO salesDetailVO = BeanUtil.copy(salesDetail, SalesDetailVO.class);
		if (Func.isNotEmpty(salesDetailVO)) {
			//单据状态名称
			salesDetailVO.setDetailStatusName(DictCache.getValue(DictCodeConstant.OUTSTOCK_STATUS, salesDetailVO.getBillDetailState()));
			//包装名称
			SkuPackage skuPackage = SkuPackageCache.getById(salesDetailVO.getWspId());
			if (Func.isNotEmpty(skuPackage)) {
				salesDetailVO.setWspName(skuPackage.getWspName());
			}
			Sku sku = SkuCache.getById(salesDetailVO.getSkuId());
			//是否序列号
			if (Func.isNotEmpty(sku)) {
				ISkuTypeService skuTypeService = SpringUtil.getBean(ISkuTypeService.class);
				SkuType skuType = skuTypeService.getById(sku.getSkuTypeId());
				if (Func.isNotEmpty(skuType)) {
					salesDetailVO.setSkuType(skuType.getTypeName());
				}
				salesDetailVO.setIsSn(DictCache.getValue(DictCodeConstant.IS_SN, sku.getIsSn()));
			} else {
				salesDetailVO.setIsSn(StringPool.CHS_NO);
			}
			// 包装层级
			salesDetailVO.setSkuLevelName(DictCache.getValue(DictCodeConstant.SKU_LEVEL, salesDetailVO.getSkuLevel()));
			//计划数量
			salesDetailVO.setPlanQtyName(SkuPackageDetailCache.convert(salesDetailVO.getWspId(),
				salesDetailVO.getSkuLevel(), salesDetailVO.getPlanQty()));
			//实际数量
			salesDetailVO.setScanQtyName(SkuPackageDetailCache.convert(salesDetailVO.getWspId(),
				salesDetailVO.getSkuLevel(), salesDetailVO.getScanQty()));
			//剩余数量
			salesDetailVO.setSurplusQtyName(SkuPackageDetailCache.convert(salesDetailVO.getWspId(),
				salesDetailVO.getSkuLevel(), salesDetailVO.getSurplusQty()));
			//包装明细ID
			if (Func.isNotEmpty(salesDetailVO.getSkuLevel())) {
				SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(
					salesDetailVO.getWspId(), salesDetailVO.getSkuLevel());
				if (ObjectUtil.isNotEmpty(skuPackageDetail)) {
					salesDetailVO.setWspdId(skuPackageDetail.getWspdId());
				}
			}

			salesDetailVO.setPlanQty(salesDetailVO.getPlanQty().divide(new BigDecimal(salesDetailVO.getConvertQty())));
		}
		return salesDetailVO;
	}
}
