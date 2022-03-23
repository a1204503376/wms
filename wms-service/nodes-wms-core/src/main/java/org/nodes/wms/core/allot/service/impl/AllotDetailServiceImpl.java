
package org.nodes.wms.core.allot.service.impl;

import org.nodes.wms.core.allot.entity.AllotDetail;
import org.nodes.wms.core.allot.entity.AllotHeader;
import org.nodes.wms.core.allot.mapper.AllotDetailMapper;
import org.nodes.wms.core.allot.service.IAllotDetailService;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 调拨明细表 服务实现类
 *
 * @author Wangjw
 * @since 2020-02-10
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class AllotDetailServiceImpl<M extends AllotDetailMapper, T extends AllotHeader>
	extends BaseServiceImpl<AllotDetailMapper, AllotDetail>
	implements IAllotDetailService {

	@Override
	public Boolean save(Long allotBillId, List<AllotDetail> detailList) {
		Integer index = 1;
		for (AllotDetail allotDetail : detailList) {
			//通过包装id获得包装明细列表
			List<SkuPackageDetail> skuPackageDetailList = SkuPackageDetailCache.listByWspId(allotDetail.getWspId());

			for (SkuPackageDetail skuPackageDetail : skuPackageDetailList) {
				if (Func.isEmpty(skuPackageDetail.getWsuCode())) {
					throw new ServiceException("基础信息错误：计量单位编码为空！");
				}
				if (skuPackageDetail.getWsuCode().equals(allotDetail.getUmCode())) {
					//换算倍率
					allotDetail.setConvertQty(skuPackageDetail.getConvertQty());
					allotDetail.setAllotPlanQty(allotDetail.getAllotPlanQty()
						.multiply(new BigDecimal(skuPackageDetail.getConvertQty())));
					//规格
//					allotDetail.setSkuSpec(skuPackageDetail.getSkuSpec());
					//包装层级
					allotDetail.setSkuLevel(skuPackageDetail.getSkuLevel());
				}
				if (new Integer(1).equals(skuPackageDetail.getSkuLevel())) {
					//基础计量单位编码
					allotDetail.setBaseUmCode(skuPackageDetail.getWsuCode());
					//基础计量单位名称
					allotDetail.setBaseUmName(skuPackageDetail.getWsuName());
				}
			}
			//从缓存中获得物品信息
			Sku sku = SkuCache.getById(allotDetail.getSkuId());
			if (Func.isEmpty(sku)) {
				throw new ServiceException("物品[ID: " + allotDetail.getSkuId());
			}
			//物品编码
			allotDetail.setSkuCode(sku.getSkuCode());
			//物品名称
			allotDetail.setSkuName(sku.getSkuName());

			//行号
			allotDetail.setAllotLineNo(String.format("%08d", index));
			allotDetail.setAllotBillId(allotBillId);
			index++;
		}
		return super.saveBatch(detailList, detailList.size());
	}

	@Override
	public Boolean update(Long allotBillId, List<AllotDetail> detailList) {
		Integer index = 1;
		for (AllotDetail allotDetail : detailList) {
			//通过包装id获得包装明细列表
			List<SkuPackageDetail> skuPackageDetailList = SkuPackageDetailCache.listByWspId(allotDetail.getWspId());

			for (SkuPackageDetail skuPackageDetail : skuPackageDetailList) {
				if (Func.isEmpty(skuPackageDetail.getWsuCode())) {
					throw new ServiceException("基础信息错误：计量单位编码为空！");
				}
				if (skuPackageDetail.getWsuCode().equals(allotDetail.getUmCode())) {
					//换算倍率
					allotDetail.setConvertQty(skuPackageDetail.getConvertQty());
					allotDetail.setAllotPlanQty(allotDetail.getAllotPlanQty()
						.multiply(new BigDecimal(skuPackageDetail.getConvertQty())));
					//规格
					allotDetail.setSkuSpec(skuPackageDetail.getSkuSpec());
					//包装层级
					allotDetail.setSkuLevel(skuPackageDetail.getSkuLevel());
				}
				if (new Integer(1).equals(skuPackageDetail.getSkuLevel())) {
					//基础计量单位编码
					allotDetail.setBaseUmCode(skuPackageDetail.getWsuCode());
					//基础计量单位名称
					allotDetail.setBaseUmName(skuPackageDetail.getWsuName());
				}
			}
			//从缓存中获得物品信息
			Sku sku = SkuCache.getById(allotDetail.getSkuId());
			if (Func.isEmpty(sku)) {
				throw new ServiceException("物品[ID: " + allotDetail.getSkuId());
			}
			//物品编码
			allotDetail.setSkuCode(sku.getSkuCode());
			//物品名称
			allotDetail.setSkuName(sku.getSkuName());

			//行号
			allotDetail.setAllotLineNo(String.format("%08d", index));
			allotDetail.setAllotBillId(allotBillId);
		}
		return super.saveOrUpdateBatch(detailList, detailList.size());
	}
}
