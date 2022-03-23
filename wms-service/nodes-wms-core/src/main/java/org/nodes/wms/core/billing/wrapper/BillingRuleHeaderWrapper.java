package org.nodes.wms.core.billing.wrapper;

import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.nodes.wms.core.billing.entity.BillingRuleHeader;
import org.nodes.wms.core.billing.vo.BillingRuleHeaderVO;


import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.nodes.wms.core.billing.service.IBillingRuleHeaderService;


/**
 * 计费规则主表包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-06-19
 */
public class BillingRuleHeaderWrapper extends BaseEntityWrapper<BillingRuleHeader, BillingRuleHeaderVO>  {

	public static BillingRuleHeaderWrapper build() {
		return new BillingRuleHeaderWrapper();
 	}

	@Override
	public BillingRuleHeaderVO entityVO(BillingRuleHeader billingRuleHeader) {
		BillingRuleHeaderVO billingRuleHeaderVO = BeanUtil.copy(billingRuleHeader, BillingRuleHeaderVO.class);
        if (Func.isNotEmpty(billingRuleHeaderVO)) {
			Warehouse warehouse = WarehouseCache.getById(billingRuleHeaderVO.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				billingRuleHeaderVO.setWhCode(warehouse.getWhCode());
				billingRuleHeaderVO.setWhName(warehouse.getWhName());
			}
        }
		return billingRuleHeaderVO;
	}
}
