package org.nodes.wms.core.instock.asn.service.impl;

import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.instock.asn.dto.AsnDetailDTO;
import org.nodes.wms.core.instock.asn.entity.AsnDetail;
import org.nodes.wms.core.instock.asn.entity.Sn;
import org.nodes.wms.core.instock.asn.mapper.AsnDetailMapper;
import org.nodes.wms.core.instock.asn.service.IAsnDetailService;
import org.nodes.wms.core.instock.asn.service.ISnService;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 收货单明细表 服务实现类
 *
 * @author zx
 * @since 2019-12-13
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class AsnDetailServiceImpl<M extends AsnDetailMapper, T extends AsnDetail>
	extends BaseServiceImpl<AsnDetailMapper, AsnDetail>
	implements IAsnDetailService {

	@Autowired
	ISnService snService;

	@Override
	public boolean saveOrUpdate(AsnDetailDTO asnDetail) {
		//上位系统单据明细唯一标识
//		if (Func.isEmpty(asnDetail.getAsnBillDetailKey())) {
//			asnDetail.setAsnBillDetailKey(asnDetail.getAsnLineNo());
//		}
		//物品
		Sku sku = SkuCache.getById(asnDetail.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("物品不存在或已经删除");
		}
		//物品编码
		asnDetail.setSkuCode(sku.getSkuCode());
		//物品名称
		asnDetail.setSkuName(sku.getSkuName());
		SkuPackage skuPackage = SkuPackageCache.getById(asnDetail.getWspId());
		if (Func.isEmpty(skuPackage)) {
			throw new ServiceException("指定包装不存在(ID::" + asnDetail.getWspId() + ")");
		}
		//规格
		asnDetail.setSkuSpec(skuPackage.getSpec());
		// 获取该包装下包装明细
		List<SkuPackageDetail> skuPackageDetailList = SkuPackageDetailCache.listByWspId(skuPackage.getWspId());
		if (Func.isEmpty(skuPackageDetailList)) {
			throw new ServiceException("包装:" + skuPackage.getWspName() + " 下没有包装明细！");
		}
		// 获取基础计量单位的包装明细
		SkuPackageDetail basePackageDetail = skuPackageDetailList.stream().filter(u -> {
			return u.getSkuLevel().equals(1);
		}).findFirst().orElse(null);
		if (Func.isEmpty(basePackageDetail)) {
			throw new ServiceException("包装:" + skuPackage.getWspName() + " 下不存在 基础计量单位 的层级！");
		}
		asnDetail.setBaseUmCode(basePackageDetail.getWsuCode());
		asnDetail.setBaseUmName(basePackageDetail.getWsuName());
		// 获取业务包装明细
		SkuPackageDetail skuPackageDetail = skuPackageDetailList.stream().filter(u -> {
			return u.getWsuCode().equals(asnDetail.getUmCode());
		}).findFirst().orElse(null);
		if (Func.isEmpty(skuPackageDetail)) {
			throw new ServiceException(
				"包装:" + skuPackage.getWspName() + " 下不存在计量单位编码为 " + asnDetail.getUmCode() + " 的包装明细！");
		}
		//层级
		asnDetail.setSkuLevel(skuPackageDetail.getSkuLevel());
		//换算倍率
		asnDetail.setConvertQty(skuPackageDetail.getConvertQty());
		//计量单位编码
		asnDetail.setUmCode(skuPackageDetail.getWsuCode());
		//计量单位名称
		asnDetail.setUmName(skuPackageDetail.getWsuName());

		//计划数量 换算成最小计量单位的数量
		if (Func.isEmpty(asnDetail.getPlanQty())) {
			throw new ServiceException(String.format("计划数量不能为空"));
		}
		asnDetail.setPlanQty(asnDetail.getPlanQty().multiply(new BigDecimal(asnDetail.getConvertQty())));
		if (Func.isEmpty(asnDetail.getScanQty())) {
			//实际数量
			asnDetail.setScanQty(BigDecimal.ZERO);
		}
		//剩余数量
		asnDetail.setSurplusQty(asnDetail.getPlanQty());
		if (Func.isEmpty(asnDetail.getDetailStatus())) {
			//接收状态
			asnDetail.setDetailStatus(10);
		}
		//序列号导入状态
		asnDetail.setImportSn(StringPool.N.toUpperCase());

		boolean result = super.saveOrUpdate(asnDetail);
		//生成序列号
		if (Func.isNotEmpty(asnDetail.getSerialList())) {
			asnDetail.setImportSn(StringPool.Y.toUpperCase());
			List<Sn> snList = new ArrayList();
			for (String serial : asnDetail.getSerialList()) {
				Sn sn = new Sn();
				sn.setSnStatus(StringPool.ONE);
				sn.setAsnBillId(asnDetail.getAsnBillId());
				sn.setSnDetailCode(serial);
				sn.setAsnDetailId(asnDetail.getAsnDetailId());
				snList.add(sn);
			}
			snService.saveBatch(snList, snList.size());
		}

		return result;
	}

	@Override
	public boolean saveOrUpdateByAllot(AsnDetailDTO asnDetail) {
		//上位系统单据明细唯一标识
//		if (Func.isEmpty(asnDetail.getAsnBillDetailKey())) {
//			asnDetail.setAsnBillDetailKey(asnDetail.getAsnLineNo());
//		}
		//物品
		Sku sku = SkuCache.getById(asnDetail.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("物品不存在或已经删除");
		}
		//物品编码
		asnDetail.setSkuCode(sku.getSkuCode());
		//物品名称
		asnDetail.setSkuName(sku.getSkuName());
		SkuPackage skuPackage = SkuPackageCache.getById(asnDetail.getWspId());
		if (Func.isEmpty(skuPackage)) {
			throw new ServiceException("指定包装不存在(ID::" + asnDetail.getWspId() + ")");
		}
		//规格
		asnDetail.setSkuSpec(skuPackage.getSpec());
		// 获取该包装下包装明细
		List<SkuPackageDetail> skuPackageDetailList = SkuPackageDetailCache.listByWspId(skuPackage.getWspId());
		if (Func.isEmpty(skuPackageDetailList)) {
			throw new ServiceException("包装:" + skuPackage.getWspName() + " 下没有包装明细！");
		}
		// 获取基础计量单位的包装明细
		SkuPackageDetail basePackageDetail = skuPackageDetailList.stream().filter(u -> {
			return u.getSkuLevel().equals(1);
		}).findFirst().orElse(null);
		if (Func.isEmpty(basePackageDetail)) {
			throw new ServiceException("包装:" + skuPackage.getWspName() + " 下不存在 基础计量单位 的层级！");
		}
		asnDetail.setBaseUmCode(basePackageDetail.getWsuCode());
		asnDetail.setBaseUmName(basePackageDetail.getWsuName());
		// 获取业务包装明细
		SkuPackageDetail skuPackageDetail = skuPackageDetailList.stream().filter(u -> {
			return u.getWsuCode().equals(asnDetail.getUmCode());
		}).findFirst().orElse(null);
		if (Func.isEmpty(skuPackageDetail)) {
			throw new ServiceException(
				"包装:" + skuPackage.getWspName() + " 下不存在计量单位编码为 " + asnDetail.getUmCode() + " 的包装明细！");
		}
		//层级
		asnDetail.setSkuLevel(skuPackageDetail.getSkuLevel());
		//换算倍率
		asnDetail.setConvertQty(skuPackageDetail.getConvertQty());
		//计量单位编码
		asnDetail.setUmCode(skuPackageDetail.getWsuCode());
		//计量单位名称
		asnDetail.setUmName(skuPackageDetail.getWsuName());

		//计划数量 换算成最小计量单位的数量
		if (Func.isEmpty(asnDetail.getPlanQty())) {
			throw new ServiceException(String.format("计划数量不能为空"));
		}
		asnDetail.setPlanQty(asnDetail.getPlanQty());
		if (Func.isEmpty(asnDetail.getScanQty())) {
			//实际数量
			asnDetail.setScanQty(BigDecimal.ZERO);
		}
		//剩余数量
		asnDetail.setSurplusQty(asnDetail.getPlanQty());
		if (Func.isEmpty(asnDetail.getDetailStatus())) {
			//接收状态
			asnDetail.setDetailStatus(10);
		}
		//序列号导入状态
		asnDetail.setImportSn(StringPool.N.toUpperCase());

		boolean result = super.saveOrUpdate(asnDetail);
		//生成序列号
		if (Func.isNotEmpty(asnDetail.getSerialList())) {
			asnDetail.setImportSn(StringPool.Y.toUpperCase());
			List<Sn> snList = new ArrayList();
			for (String serial : asnDetail.getSerialList()) {
				Sn sn = new Sn();
				sn.setSnStatus(StringPool.ONE);
				sn.setAsnBillId(asnDetail.getAsnBillId());
				sn.setSnDetailCode(serial);
				sn.setAsnDetailId(asnDetail.getAsnDetailId());
				snList.add(sn);
			}
			snService.saveBatch(snList, snList.size());
		}

		return result;
	}


	@Override
	public List<AsnDetail> list(Long asnBillId) {
		AsnDetail asnDetail = new AsnDetail();
		asnDetail.setAsnBillId(asnBillId);
		return super.list(Condition.getQueryWrapper(asnDetail));
	}
}
