package org.nodes.wms.core.outstock.so.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.nodes.wms.core.outstock.sales.service.ISalesDetailService;
import org.springblade.core.log.exception.ServiceException;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.outstock.so.dto.SoDetailDTO;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.dao.outstock.so.enums.SoDetailStateEnum;
import org.nodes.wms.core.outstock.so.mapper.SoDetailMapper;
import org.nodes.wms.core.outstock.so.service.ISoDetailService;
import org.nodes.wms.core.outstock.so.wrapper.SoDetailWrapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务实现类
 *
 * @author zhonglianshuai
 * @since 2020-02-10
 */
@Slf4j
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SoDetailServiceImpl<M extends SoDetailMapper, T extends SoDetail>
	extends BaseServiceImpl<SoDetailMapper, SoDetail>
	implements ISoDetailService {

	@Autowired
	ISalesDetailService salesDetailService;

	@Override
	public boolean save(SoDetail entity) {
		Sku sku = SkuCache.getById(entity.getSkuId());
		if (Func.isNotEmpty(sku)) {
			entity.setSkuCode(sku.getSkuCode());
			entity.setSkuName(sku.getSkuName());
		}
		entity.setScanQty(BigDecimal.ZERO);
		entity.setSurplusQty(entity.getPlanQty());
		entity.setBillDetailState(SoDetailStateEnum.UnAlloc.getIndex());

		return super.save(entity);
	}

	@Override
	public synchronized boolean updateState(Long soDetailId, SoDetailStateEnum soDetailState) {
		SoDetail soDetail = super.getById(soDetailId);
		if (Func.isEmpty(soDetail)) {
			throw new ServiceException("订单明细不存在（ID：" + soDetailId + "）！");
		}
		soDetail.setBillDetailState(soDetailState.getIndex());
		return super.updateById(soDetail);
	}
	/**
	 * 出库单明细保存
	 *
	 * @return
	 */
	@Override
	public boolean save(SoDetailDTO soDetailDTO) {
		SoDetail soDetail = SoDetailWrapper.build().dto2Entity(soDetailDTO);

		if (Func.isEmpty(soDetail.getSoLineNo())) throw new ServiceException(String.format("行号不能为空"));
		if (Func.isEmpty(soDetail.getSkuId())) throw new ServiceException(String.format("物品ID不能为空"));
		if (Func.isEmpty(soDetail.getWspId())) throw new ServiceException(String.format("包装ID不能为空"));
//		if (Func.isEmpty(soDetailSubmitVO.getWspdId())) throw new ServiceException(String.format("包装明细ID不能为空"));
		if (Func.isEmpty(soDetail.getPlanQty())) throw new ServiceException(String.format("计划数量不能为空"));

		//上位系统单据唯一标识
		if (Func.isEmpty(soDetail.getBillDetailKey())) {
			soDetail.setBillDetailKey(soDetail.getSoLineNo());
		}
		//物品
		Sku sku = SkuCache.getById(soDetail.getSkuId());
		if (Func.isNotEmpty(sku)) {
			//物品编码
			soDetail.setSkuCode(sku.getSkuCode());
			//物品名称
			soDetail.setSkuName(sku.getSkuName());
		} else {
			throw new ServiceException("物品不存在或已经删除");
		}
		//包装明细
//		SkuPackageDetail skuPackageDetail = SkuPackageCache.getDetail(soDetailSubmitVO.getWspId(),soDetail.getSkuLevel());
		List<SkuPackageDetail> skuPackageDetailList = SkuPackageDetailCache.listByWspId(soDetail.getWspId()).stream()
			.filter(u -> {
				return Func.isNotEmpty(u.getWsuCode()) && u.getWsuCode().equals(soDetail.getUmCode());
			}).collect(Collectors.toList());
		if (Func.isNotEmpty(skuPackageDetailList)) {
			soDetail.setSkuLevel(skuPackageDetailList.get(0).getSkuLevel()); //层级
//			soDetail.setSkuSpec(skuPackageDetailList.get(0).getSkuSpec());//规格
			soDetail.setConvertQty(skuPackageDetailList.get(0).getConvertQty());//换算倍率
			soDetail.setUmCode(skuPackageDetailList.get(0).getWsuCode());//计量单位编码
			soDetail.setUmName(skuPackageDetailList.get(0).getWsuName());//计量单位名称
		} else {
			throw new ServiceException("包装明细不存在或已经删除");
		}
		//包装明细列表
		List<SkuPackageDetail> packageDetails = SkuPackageDetailCache.listByWspId(soDetail.getWspId())
			.stream()
			.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel))
			.collect(Collectors.toList());
		for (SkuPackageDetail packageDetail : packageDetails) {
			if (1 == packageDetail.getSkuLevel()) {
				soDetail.setBaseUmCode(packageDetail.getWsuCode());//基础计量单位编码
				soDetail.setBaseUmName(packageDetail.getWsuName());//基础计量单位名称
			}
		}
		if(Func.isEmpty(soDetail.getBaseUmCode())){
			throw new ServiceException("未找到当前订单的基础计量单位，请检查是否异常数据");
		}
		//计划数量
		soDetail.setPlanQty(soDetail.getPlanQty().multiply(new BigDecimal(soDetail.getConvertQty())));
		//实际数量
		soDetail.setScanQty(BigDecimal.ZERO);
		//剩余数量
		soDetail.setSurplusQty(soDetail.getPlanQty());
		//单据状态
		soDetail.setBillDetailState(10);

		ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(soDetail);
		if (validResult.hasErrors()) {
			throw new ServiceException(validResult.getErrors());
		}
		super.save(soDetail);
		return true;
	}

	/**
	 * 出库单明细保存
	 *
	 * @return
	 */
	@Override
	public boolean saveByAllot(SoDetailDTO soDetailDTO) {
		SoDetail soDetail = SoDetailWrapper.build().dto2Entity(soDetailDTO);

		if (Func.isEmpty(soDetail.getSoLineNo())) throw new ServiceException(String.format("行号不能为空"));
		if (Func.isEmpty(soDetail.getSkuId())) throw new ServiceException(String.format("物品ID不能为空"));
		if (Func.isEmpty(soDetail.getWspId())) throw new ServiceException(String.format("包装ID不能为空"));
//		if (Func.isEmpty(soDetailSubmitVO.getWspdId())) throw new ServiceException(String.format("包装明细ID不能为空"));
		if (Func.isEmpty(soDetail.getPlanQty())) throw new ServiceException(String.format("计划数量不能为空"));

		//上位系统单据唯一标识
		if (Func.isEmpty(soDetail.getBillDetailKey())) {
			soDetail.setBillDetailKey(soDetail.getSoLineNo());
		}
		//物品
		Sku sku = SkuCache.getById(soDetail.getSkuId());
		if (Func.isNotEmpty(sku)) {
			//物品编码
			soDetail.setSkuCode(sku.getSkuCode());
			//物品名称
			soDetail.setSkuName(sku.getSkuName());
		} else {
			throw new ServiceException("物品不存在或已经删除");
		}
		//包装明细
//		SkuPackageDetail skuPackageDetail = SkuPackageCache.getDetail(soDetailSubmitVO.getWspId(),soDetail.getSkuLevel());
		List<SkuPackageDetail> skuPackageDetailList = SkuPackageDetailCache.listByWspId(soDetail.getWspId()).stream()
			.filter(u -> {
				return Func.isNotEmpty(u.getWsuCode()) && u.getWsuCode().equals(soDetail.getUmCode());
			}).collect(Collectors.toList());
		if (Func.isNotEmpty(skuPackageDetailList)) {
			soDetail.setSkuLevel(skuPackageDetailList.get(0).getSkuLevel()); //层级
//			soDetail.setSkuSpec(skuPackageDetailList.get(0).getSkuSpec());//规格
			soDetail.setConvertQty(skuPackageDetailList.get(0).getConvertQty());//换算倍率
			soDetail.setUmCode(skuPackageDetailList.get(0).getWsuCode());//计量单位编码
			soDetail.setUmName(skuPackageDetailList.get(0).getWsuName());//计量单位名称
		} else {
			throw new ServiceException("包装明细不存在或已经删除");
		}
		//包装明细列表
		List<SkuPackageDetail> packageDetails = SkuPackageDetailCache.listByWspId(soDetail.getWspId())
			.stream()
			.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel))
			.collect(Collectors.toList());
		for (SkuPackageDetail packageDetail : packageDetails) {
			if (1 == packageDetail.getSkuLevel()) {
				soDetail.setBaseUmCode(packageDetail.getWsuCode());//基础计量单位编码
				soDetail.setBaseUmName(packageDetail.getWsuName());//基础计量单位名称
			}
		}
		//计划数量
		soDetail.setPlanQty(soDetail.getPlanQty());
		//实际数量
		soDetail.setScanQty(BigDecimal.ZERO);
		//剩余数量
		soDetail.setSurplusQty(soDetail.getPlanQty());
		//单据状态
		soDetail.setBillDetailState(10);

		ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(soDetail);
		if (validResult.hasErrors()) {
			throw new ServiceException(validResult.getErrors());
		}

		super.saveOrUpdate(soDetail);
		return true;
	}

	/**
	 * 更新发货明细数量
	 *
	 * @param soDetail
	 * @param count
	 * @return
	 */
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public synchronized boolean updateSoDetailQty(SoDetail soDetail, BigDecimal count) {
		BigDecimal planQty = soDetail.getPlanQty();
		BigDecimal scanQty = soDetail.getScanQty();
		BigDecimal surplusQty = soDetail.getSurplusQty();
		if (BigDecimalUtil.gt(count,surplusQty)) {
			scanQty = scanQty.add(planQty.subtract(scanQty));
			surplusQty = planQty.subtract(scanQty);
			count = count.subtract(surplusQty);
		} else {
			scanQty = scanQty.add(count);
			surplusQty = planQty.subtract(scanQty);
			count = BigDecimal.ZERO;
		}
		count = count.subtract(surplusQty);
		LambdaUpdateWrapper<SoDetail> wrapper = Wrappers.lambdaUpdate(SoDetail.class)
			.set(SoDetail::getScanQty, scanQty)
			.set(SoDetail::getSurplusQty, surplusQty)
			.eq(SoDetail::getSoDetailId, soDetail.getSoDetailId());
		soDetail.setScanQty(scanQty);
		soDetail.setSurplusQty(surplusQty);
		// 更新销售订单明细
		salesDetailService.updateQty(soDetail);
		super.update(wrapper);

		return true;
	}

}
