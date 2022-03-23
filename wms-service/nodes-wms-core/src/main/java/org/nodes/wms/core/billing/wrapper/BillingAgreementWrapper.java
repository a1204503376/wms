package org.nodes.wms.core.billing.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.billing.cache.BillingRuleHeaderCache;
import org.nodes.wms.core.billing.entity.BillingRuleHeader;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.nodes.wms.core.billing.entity.BillingAgreement;
import org.nodes.wms.core.billing.vo.BillingAgreementVO;


import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.nodes.wms.core.billing.service.IBillingAgreementService;


/**
 * 计费协议包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-06-19
 */
public class BillingAgreementWrapper extends BaseEntityWrapper<BillingAgreement, BillingAgreementVO>  {

	public static BillingAgreementWrapper build() {
		return new BillingAgreementWrapper();
 	}

	@Override
	public BillingAgreementVO entityVO(BillingAgreement entity) {
		BillingAgreementVO billingAgreementVO = BeanUtil.copy(entity, BillingAgreementVO.class);
        if (Func.isNotEmpty(billingAgreementVO)) {
			BillingRuleHeader ruleHeader = BillingRuleHeaderCache.getById(billingAgreementVO.getRuleHeaderId());
			if (Func.isNotEmpty(ruleHeader)) {
				billingAgreementVO.setRuleHeaderName(ruleHeader.getBillingName());
			}
        }
        billingAgreementVO.setExecuteStateName(DictCache.getValue(DictConstant.BILLING_EXECUTE_STATE, entity.getExecuteState()));
        billingAgreementVO.setAgreementStateName(DictCache.getValue(DictConstant.BILLING_AGREEMENT_STATE, entity.getAgreementState()));

        billingAgreementVO.setIsAutoName(entity.getIsAuto() ? StringPool.CHS_YES : StringPool.CHS_NO);
		return billingAgreementVO;
	}
}
