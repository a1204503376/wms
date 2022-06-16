package org.nodes.wms.core.instock.asn.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.nodes.wms.dao.basics.sku.enums.SkuLevelEnum;
import org.nodes.wms.core.basedata.service.ISkuLotService;
import org.nodes.wms.core.basedata.service.ISkuTypeService;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;
import org.nodes.wms.core.instock.asn.dto.AsnDetailDTO;
import org.nodes.wms.core.instock.asn.entity.AsnDetail;
import org.nodes.wms.core.instock.asn.vo.AsnDetailVO;
import org.nodes.wms.core.instock.purchase.dto.PoDetailDTO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.List;

/**
 * 收货单明细表封装类
 *
 * @Author zx
 * @Date 2019/12/25
 **/
public class AsnDetailWrapper extends BaseEntityWrapper<AsnDetail, AsnDetailVO> {


	public static AsnDetailWrapper build() {
		return new AsnDetailWrapper();
	}

	@Override
	public AsnDetailVO entityVO(AsnDetail entity) {
		AsnDetailVO asnDetailVO = BeanUtil.copy(entity, AsnDetailVO.class);
		if (ObjectUtil.isNotEmpty(asnDetailVO)) {
			//接收状态
			asnDetailVO.setDetailStatusName(DictCache.getValue(DictConstant.DETAIL_STATUS, asnDetailVO.getDetailStatus()));
			//包装名称
			SkuPackage skuPackage = SkuPackageCache.getById(entity.getWspId());
			if (Func.isNotEmpty(skuPackage)) {
				asnDetailVO.setWspName(skuPackage.getWspName());
			}
			//是否序列号
			Sku sku = SkuCache.getById(entity.getSkuId());
			if (Func.isNotEmpty(sku)) {
				if (Func.isNotEmpty(sku.getIsSn())) {
					asnDetailVO.setIsSn(DictCache.getValue(DictConstant.IS_SN, sku.getIsSn()));
				} else {
					asnDetailVO.setIsSn(StringPool.CHS_NO);
				}
				ISkuTypeService skuTypeService = SpringUtil.getBean(ISkuTypeService.class);
				SkuType skuType = skuTypeService.getById(sku.getSkuTypeId());
				if (Func.isNotEmpty(skuType)) {
//					asnDetailVO.setSkuType(skuType.getTypeName());
				}
			}
			if (Func.isNotEmpty(entity.getImportSn())) {
				asnDetailVO.setImportSnName(StringPool.Y.toUpperCase().equals(entity.getImportSn()) ? "已导入" : "未导入");
			} else {
				asnDetailVO.setImportSnName("未导入");
			}
			//层级
			asnDetailVO.setSkuLevelName(DictCache.getValue(DictConstant.SKU_LEVEL, asnDetailVO.getSkuLevel()));
			//计划数量
			asnDetailVO.setPlanQtyName(SkuPackageDetailCache.convert1(asnDetailVO.getWspId(),
				asnDetailVO.getSkuLevel(), asnDetailVO.getPlanQty(), true));
			//实际数量
			asnDetailVO.setScanQtyName(SkuPackageDetailCache.convert1(asnDetailVO.getWspId(),
				asnDetailVO.getSkuLevel(), asnDetailVO.getScanQty(), true));
			//剩余数量
			asnDetailVO.setSurplusQtyName(SkuPackageDetailCache.convert1(asnDetailVO.getWspId(),
				asnDetailVO.getSkuLevel(), asnDetailVO.getSurplusQty(), true));

			//包装明细ID
			if (Func.isNotEmpty(asnDetailVO.getSkuLevel())) {
				SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(
					asnDetailVO.getWspId(), asnDetailVO.getSkuLevel());
				if (ObjectUtil.isNotEmpty(skuPackageDetail))
					asnDetailVO.setWspdId(skuPackageDetail.getWspdId());
			}
			//计划数量
			asnDetailVO.setPlanQty(asnDetailVO.getPlanQty());
			SkuPackageDetail one = SkuPackageDetailCache.getOne(asnDetailVO.getWspId(), SkuLevelEnum.Base.getIndex());
			if (Func.isNotEmpty(one)) {
				asnDetailVO.setUmCode(one.getWsuCode());
				asnDetailVO.setUmName(one.getWsuName());
			}
			List<SkuLotConfigVO> skuConfigs = SpringUtil.getBean(ISkuLotService.class).getConfig(asnDetailVO.getSkuId());
			asnDetailVO.setSkuLotList(skuConfigs);
		}
		return asnDetailVO;
	}

	public AsnDetailDTO entityDTO(PoDetailDTO poDetail) {
		AsnDetailDTO asnDetail = BeanUtil.copy(poDetail, AsnDetailDTO.class);
		if (Func.isNotEmpty(asnDetail)) {
//			asnDetail.setAsnBillDetailKey(Func.toStr(poDetail.getPoDetailId()));
		}
		return asnDetail;
	}
}
