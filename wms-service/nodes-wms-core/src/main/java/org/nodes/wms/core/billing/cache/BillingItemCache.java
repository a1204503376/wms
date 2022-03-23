package org.nodes.wms.core.billing.cache;

import org.nodes.wms.core.billing.entity.BillingItem;
import org.nodes.wms.core.billing.service.IBillingItemService;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * author: pengwei
 * date: 2021/7/1 09:16
 * description: BillingItemCache
 */
public class BillingItemCache {

	static final Map<Long, BillingItem> cache = new HashMap<>();



	public static BillingItem getById(Serializable id) {
		return cache.getOrDefault(id, null);
	}

	/*public static void saveOrUpdate(BillingItem entity) {
		BillingItem item = getById(entity.getId());
		if (Func.isNotEmpty(item)) {
			cache.replace(entity.getId(), entity);
		} else {
			cache.put(entity.getId(), entity);
		}
	}

	public static void removeById(Serializable id) {
		cache.remove(id);
	}

	public static void removeByIds(Collection<? extends Serializable> idList) {
		cache.values().removeIf(new Predicate<BillingItem>() {
			@Override
			public boolean test(BillingItem billingItem) {
				return idList.contains(billingItem.getId());
			}
		});
	}*/
}
