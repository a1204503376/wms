package org.nodes.wms.core.outstock.so.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.dao.basics.sku.entities.SkuType;
import org.nodes.wms.core.basedata.service.ISkuTypeService;
import org.nodes.wms.core.outstock.so.dto.SoDetailDTO;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.vo.SoDetailVO;
import org.nodes.wms.core.outstock.sales.dto.SalesDetailDTO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 出库单明细表 封装类
 *
 * @Author zhonglianshuai
 * @Date 2020/02/11
 **/
public class SoDetailWrapper extends BaseEntityWrapper<SoDetail, SoDetailVO> {

	public static SoDetailWrapper build() {
		return new SoDetailWrapper();
	}

	@Override
	public SoDetailVO entityVO(SoDetail entity) {
		SoDetailVO soDetailVO = BeanUtil.copy(entity, SoDetailVO.class);
		if (Func.isNotEmpty(soDetailVO)) {
			//单据状态名称
			soDetailVO.setDetailStatusName(DictCache.getValue(DictConstant.OUTSTOCK_STATUS, soDetailVO.getBillDetailState()));
			//包装名称
			SkuPackage skuPackage = SkuPackageCache.getById(entity.getWspId());
			if (Func.isNotEmpty(skuPackage)) {
				soDetailVO.setWspName(skuPackage.getWspName());
			}
			Sku sku = SkuCache.getById(entity.getSkuId());
			//是否序列号
			if (Func.isNotEmpty(sku)) {
				ISkuTypeService skuTypeService = SpringUtil.getBean(ISkuTypeService.class);
				SkuType skuType = skuTypeService.getById(sku.getSkuTypeId());
				if (Func.isNotEmpty(skuType)) {
					soDetailVO.setSkuType(skuType.getTypeName());
				}
				soDetailVO.setIsSn(DictCache.getValue(DictConstant.IS_SN, sku.getIsSn()));
			} else {
				soDetailVO.setIsSn(StringPool.CHS_NO);
			}
			// 包装层级
			soDetailVO.setSkuLevelName(DictCache.getValue(DictConstant.SKU_LEVEL, entity.getSkuLevel()));
			//计划数量
			soDetailVO.setPlanQtyName(SkuPackageDetailCache.convert1(soDetailVO.getWspId(),
				soDetailVO.getSkuLevel(), soDetailVO.getPlanQty(), true));
			//实际数量
			soDetailVO.setScanQtyName(SkuPackageDetailCache.convert1(soDetailVO.getWspId(),
				soDetailVO.getSkuLevel(), soDetailVO.getScanQty(), true));
			//剩余数量
			soDetailVO.setSurplusQtyName(SkuPackageDetailCache.convert1(soDetailVO.getWspId(),
				soDetailVO.getSkuLevel(), soDetailVO.getSurplusQty(), true));
			//包装明细ID
			if (Func.isNotEmpty(soDetailVO.getSkuLevel())) {
				SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(
					soDetailVO.getWspId(), soDetailVO.getSkuLevel());
				if (ObjectUtil.isNotEmpty(skuPackageDetail)) {
					soDetailVO.setWspdId(skuPackageDetail.getWspdId());
				}
			}

			soDetailVO.setPlanQty(soDetailVO.getPlanQty().divide(new BigDecimal(soDetailVO.getConvertQty()), BigDecimal.ROUND_HALF_UP));
		}
		return soDetailVO;
	}

	public SoDetailDTO entityDTO(SalesDetailDTO salesDetail) {
		SoDetailDTO soDetail = BeanUtil.copy(salesDetail, SoDetailDTO.class);
		if (Func.isNotEmpty(soDetail)) {
			soDetail.setBillDetailKey(Func.toStr(soDetail.getSoDetailId()));
		}
		return soDetail;
	}

	public SoDetail dto2Entity(SoDetailDTO dto) {
		return Objects.requireNonNull(BeanUtil.copy(dto, SoDetail.class));
	}
}
