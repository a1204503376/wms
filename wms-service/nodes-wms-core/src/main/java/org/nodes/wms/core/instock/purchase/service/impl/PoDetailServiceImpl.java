package org.nodes.wms.core.instock.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.instock.asn.entity.AsnDetail;
import org.nodes.wms.core.instock.purchase.dto.PoDetailDTO;
import org.nodes.wms.core.instock.purchase.entity.PoDetail;
import org.nodes.wms.core.instock.purchase.mapper.PoDetailMapper;
import org.nodes.wms.core.instock.purchase.service.IPoDetailService;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringPool;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 收货单明细表 服务实现类
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class PoDetailServiceImpl<M extends PoDetailMapper, T extends PoDetail>
	extends BaseServiceImpl<PoDetailMapper, PoDetail>
	implements IPoDetailService {

	@Override
	public boolean saveOrUpdate(PoDetailDTO poDetail) {
		//上位系统单据明细唯一标识
		if (Func.isEmpty(poDetail.getPoBillDetailKey())) {
			poDetail.setPoBillDetailKey(poDetail.getPoLineNo());
		}
		//物品
		Sku sku = SkuCache.getById(poDetail.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("物品不存在或已经删除");
		}
		//物品编码
		poDetail.setSkuCode(sku.getSkuCode());
		//物品名称
		poDetail.setSkuName(sku.getSkuName());
		SkuPackage skuPackage = SkuPackageCache.getById(poDetail.getWspId());
		if (Func.isEmpty(skuPackage)) {
			throw new ServiceException("指定包装不存在(ID::" + poDetail.getWspId() + ")");
		}
		//规格
		poDetail.setSkuSpec(skuPackage.getSpec());
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
		poDetail.setBaseUmCode(basePackageDetail.getWsuCode());
		poDetail.setBaseUmName(basePackageDetail.getWsuName());
		// 获取业务包装明细
		SkuPackageDetail skuPackageDetail = skuPackageDetailList.stream().filter(u -> {
			return u.getWsuCode().equals(poDetail.getUmCode());
		}).findFirst().orElse(null);
		if (Func.isEmpty(skuPackageDetail)) {
			throw new ServiceException(
				"包装:" + skuPackage.getWspName() + " 下不存在计量单位编码为 " + poDetail.getUmCode() + " 的包装明细！");
		}
		//层级
		poDetail.setSkuLevel(skuPackageDetail.getSkuLevel());
		//换算倍率
		poDetail.setConvertQty(skuPackageDetail.getConvertQty());
		//计量单位编码
		poDetail.setUmCode(skuPackageDetail.getWsuCode());
		//计量单位名称
		poDetail.setUmName(skuPackageDetail.getWsuName());

		//计划数量 换算成最小计量单位的数量
		if (Func.isEmpty(poDetail.getPlanQty())) {
			throw new ServiceException(String.format("计划数量不能为空"));
		}
		poDetail.setPlanQty(poDetail.getPlanQty().multiply(new BigDecimal(poDetail.getConvertQty())));
		if (Func.isEmpty(poDetail.getScanQty())) {
			//实际数量
			poDetail.setScanQty(BigDecimal.ZERO);
		}
		//剩余数量
		poDetail.setSurplusQty(poDetail.getPlanQty());
		if (Func.isEmpty(poDetail.getDetailStatus())) {
			//接收状态
			poDetail.setDetailStatus(10);
		}
		//序列号导入状态
		poDetail.setImportSn(StringPool.N.toUpperCase());

		return super.saveOrUpdate(poDetail);
	}

	@Override
	public boolean updateQty(AsnDetail asnDetail) {
		UpdateWrapper<PoDetail> updateWrapper = new UpdateWrapper<>();
		updateWrapper.lambda()
			.set(PoDetail::getScanQty, asnDetail.getScanQty())
			.set(PoDetail::getSurplusQty, asnDetail.getSurplusQty())
			.eq(PoDetail::getPoLineNo, asnDetail.getAsnLineNo());
//			.eq(PoDetail::getPoDetailId, asnDetail.getAsnBillDetailKey());
		return super.update(updateWrapper);
	}
}
