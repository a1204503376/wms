package org.nodes.wms.core.billing.service.impl;

import org.nodes.wms.core.basedata.cache.SkuUmCache;
import org.nodes.wms.core.basedata.entity.SkuUm;
import org.nodes.wms.core.basedata.service.ISkuUmService;
import org.nodes.wms.core.billing.cache.BillingItemCache;
import org.nodes.wms.core.billing.dto.BillingRuleDetailDTO;
import org.nodes.wms.core.billing.entity.BillingItem;
import org.nodes.wms.core.billing.entity.BillingRuleDetail;
import org.nodes.wms.core.billing.mapper.BillingRuleDetailMapper;
import org.nodes.wms.core.billing.service.IBillingRuleDetailService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 计费规则明细 服务实现类
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED,isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class BillingRuleDetailServiceImpl<M extends BillingRuleDetailMapper, T extends BillingRuleDetail>
    extends BaseServiceImpl<BillingRuleDetailMapper, BillingRuleDetail>
    implements IBillingRuleDetailService {

	@Override
	public boolean saveOrUpdateBatch(List<BillingRuleDetailDTO> entityList) {
		List<BillingRuleDetail> detailList = new ArrayList<>();
		ISkuUmService skuUmService = SpringUtil.getBean(ISkuUmService.class);
		entityList.forEach(entity->{
			SkuUm skuUm = skuUmService.getById(entity.getWsuId());
			if (Func.isNotEmpty(skuUm)) {
				entity.setWsuCode(skuUm.getWsuCode());
				entity.setWsuName(skuUm.getWsuName());
			}

			BillingItem billingItem = BillingItemCache.getById(entity.getItemId());
			if (Func.isNotEmpty(billingItem)) {
				entity.setItemCode(billingItem.getCode());
				entity.setItemName(billingItem.getName());
			}
			detailList.add(entity);
		});
		return super.saveOrUpdateBatch(detailList, detailList.size());
	}
}
