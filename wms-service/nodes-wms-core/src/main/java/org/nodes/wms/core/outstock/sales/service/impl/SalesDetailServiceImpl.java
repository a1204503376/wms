package org.nodes.wms.core.outstock.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.sales.dto.SalesDetailDTO;
import org.nodes.wms.core.outstock.sales.entity.SalesDetail;
import org.nodes.wms.core.outstock.sales.mapper.SalesDetailMapper;
import org.nodes.wms.core.outstock.sales.service.ISalesDetailService;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springblade.core.tool.utils.Func;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 发货单明细
 服务实现类
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED,isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SalesDetailServiceImpl<M extends SalesDetailMapper, T extends SalesDetail> extends BaseServiceImpl<SalesDetailMapper, SalesDetail> implements ISalesDetailService {

	@Override
	public boolean saveOrUpdate(SalesDetailDTO salesDetail) {
		if (Func.isEmpty(salesDetail.getSoLineNo())) {
			throw new ServiceException(String.format("行号不能为空"));
		}
		if (Func.isEmpty(salesDetail.getSkuId())) {
			throw new ServiceException(String.format("物品ID不能为空"));
		}
		if (Func.isEmpty(salesDetail.getWspId())) {
			throw new ServiceException(String.format("包装ID不能为空"));
		}
		if (Func.isEmpty(salesDetail.getPlanQty())) {
			throw new ServiceException(String.format("计划数量不能为空"));
		}

		//上位系统单据唯一标识
		if (Func.isEmpty(salesDetail.getBillDetailKey())) {
			salesDetail.setBillDetailKey(salesDetail.getSoLineNo());
		}
		//物品
		Sku sku = SkuCache.getById(salesDetail.getSkuId());
		if (Func.isNotEmpty(sku)) {
			//物品编码
			salesDetail.setSkuCode(sku.getSkuCode());
			//物品名称
			salesDetail.setSkuName(sku.getSkuName());
		} else {
			throw new ServiceException("物品不存在或已经删除");
		}
		//包装明细
		List<SkuPackageDetail> skuPackageDetailList = SkuPackageDetailCache.listByWspId(salesDetail.getWspId()).stream()
			.filter(u -> {
				return Func.isNotEmpty(u.getWsuCode()) && u.getWsuCode().equals(salesDetail.getUmCode());
			}).collect(Collectors.toList());
		if (Func.isNotEmpty(skuPackageDetailList)) {
			salesDetail.setSkuLevel(skuPackageDetailList.get(0).getSkuLevel()); //层级
			salesDetail.setSkuSpec(skuPackageDetailList.get(0).getSkuSpec());//规格
			salesDetail.setConvertQty(skuPackageDetailList.get(0).getConvertQty());//换算倍率
			salesDetail.setUmCode(skuPackageDetailList.get(0).getWsuCode());//计量单位编码
			salesDetail.setUmName(skuPackageDetailList.get(0).getWsuName());//计量单位名称
		} else {
			throw new ServiceException("包装明细不存在或已经删除");
		}
		//包装明细列表
		List<SkuPackageDetail> packageDetails = SkuPackageDetailCache.listByWspId(salesDetail.getWspId())
			.stream()
			.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel))
			.collect(Collectors.toList());
		for (SkuPackageDetail packageDetail : packageDetails) {
			if (1 == packageDetail.getSkuLevel()) {
				salesDetail.setBaseUmCode(packageDetail.getWsuCode());//基础计量单位编码
				salesDetail.setBaseUmName(packageDetail.getWsuName());//基础计量单位名称
			}
		}
		//计划数量
		salesDetail.setPlanQty(salesDetail.getPlanQty().multiply(new BigDecimal(salesDetail.getConvertQty())));
		//实际数量
		salesDetail.setScanQty(BigDecimal.ZERO);
		//剩余数量
		salesDetail.setSurplusQty(salesDetail.getPlanQty());
		//单据状态
		salesDetail.setBillDetailState(10);

		return super.saveOrUpdate(salesDetail);
	}

	@Override
	public boolean updateQty(SoDetail soDetail) {
		UpdateWrapper<SalesDetail> updateWrapper = new UpdateWrapper<>();
		updateWrapper.lambda()
			.set(SalesDetail::getScanQty, soDetail.getScanQty())
			.set(SalesDetail::getSurplusQty, soDetail.getSurplusQty())
			.eq(SalesDetail::getSoLineNo, soDetail.getSoLineNo())
			.eq(SalesDetail::getSoDetailId, soDetail.getBillDetailKey());
		return super.update(updateWrapper);
	}
}
