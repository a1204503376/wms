package org.nodes.wms.core.billing.cache;

import org.nodes.wms.core.billing.entity.BillingRuleHeader;
import org.nodes.wms.core.billing.service.IBillingRuleHeaderService;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * author: pengwei
 * date: 2021/7/2 09:36
 * description: BillingRuleHeaderCache
 */
public class BillingRuleHeaderCache {

	static final Map<Serializable, BillingRuleHeader> cache = new HashMap<>();

	static {
		IBillingRuleHeaderService billingRuleHeaderService = SpringUtil.getBean(IBillingRuleHeaderService.class);

		billingRuleHeaderService.list().forEach(item->{
			cache.put(item.getId(), item);
		});
	}

	public static BillingRuleHeader getById(Serializable id) {
		return cache.getOrDefault(id, null);
	}

	public static void saveOrUpdate(BillingRuleHeader entity) {
		if(Func.isNotEmpty(entity)) {
			BillingRuleHeader item = getById(entity.getId());
			if (Func.isEmpty(item)) {
				cache.put(item.getId(), item);
			} else {
				cache.replace(item.getId(), item);
			}
		}
	}

	public static void removeById(Serializable id) {
		cache.remove(id);
	}

	public static void removeByIds(Collection<? extends Serializable> idList) {
		cache.values().removeIf(new Predicate<BillingRuleHeader>() {
			@Override
			public boolean test(BillingRuleHeader billingRuleHeader) {
				return idList.contains(billingRuleHeader.getId());
			}
		});
	}
}
