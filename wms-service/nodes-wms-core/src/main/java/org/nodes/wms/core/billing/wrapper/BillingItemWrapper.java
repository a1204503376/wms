package org.nodes.wms.core.billing.wrapper;

import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.nodes.wms.core.billing.entity.BillingItem;
import org.nodes.wms.core.billing.vo.BillingItemVO;


import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.nodes.wms.core.billing.service.IBillingItemService;


/**
 * 计费项目包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-06-19
 */
public class BillingItemWrapper extends BaseEntityWrapper<BillingItem, BillingItemVO>  {

	public static BillingItemWrapper build() {
		return new BillingItemWrapper();
 	}

	@Override
	public BillingItemVO entityVO(BillingItem billingItem) {
		BillingItemVO billingItemVO = BeanUtil.copy(billingItem, BillingItemVO.class);
        if (Func.isNotEmpty(billingItemVO)) {

        }
		return billingItemVO;
	}
}
