package org.nodes.wms.core.billing.wrapper;

import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.nodes.wms.core.billing.entity.BillingRuleDetail;
import org.nodes.wms.core.billing.vo.BillingRuleDetailVO;


import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.nodes.wms.core.billing.service.IBillingRuleDetailService;


/**
 * 计费规则明细包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-06-19
 */
public class BillingRuleDetailWrapper extends BaseEntityWrapper<BillingRuleDetail, BillingRuleDetailVO>  {

	public static BillingRuleDetailWrapper build() {
		return new BillingRuleDetailWrapper();
 	}

	@Override
	public BillingRuleDetailVO entityVO(BillingRuleDetail billingRuleDetail) {
		BillingRuleDetailVO billingRuleDetailVO = BeanUtil.copy(billingRuleDetail, BillingRuleDetailVO.class);
        if (Func.isNotEmpty(billingRuleDetailVO)) {

        }
		return billingRuleDetailVO;
	}
}
