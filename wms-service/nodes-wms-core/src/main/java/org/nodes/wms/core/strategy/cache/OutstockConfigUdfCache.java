package org.nodes.wms.core.strategy.cache;

import org.nodes.wms.core.strategy.entity.OutstockConfigUdf;
import org.nodes.wms.core.strategy.service.IOutstockConfigUdfService;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author pengwei
 * @program WmsCore
 * @description 分配策略单据自定义属性缓存类
 * @since 2020-12-08
 */
public class OutstockConfigUdfCache {

	/*static Map<Long, OutstockConfigUdf> cache = new HashMap<>();

	static {
		IOutstockConfigUdfService outstockConfigUdfService = SpringUtil.getBean(IOutstockConfigUdfService.class);

		outstockConfigUdfService.list().forEach(outstockConfigUdf -> {
			cache.put(outstockConfigUdf.getSsocuId(), outstockConfigUdf);
		});
	}

	public static List<OutstockConfigUdf> listBySsodId(Long ssodId) {
		return cache.values().stream().filter(u -> {
			return u.getSsodId().equals(ssodId);
		}).collect(Collectors.toList());
	}

	public static OutstockConfigUdf getById(Long ssocuId) {
		return cache.getOrDefault(ssocuId, null);
	}

	public static void saveOrUpdate(OutstockConfigUdf outstockConfigUdf) {
		OutstockConfigUdf findObj = getById(outstockConfigUdf.getSsocuId());
		if (Func.isEmpty(findObj)) {
			cache.put(outstockConfigUdf.getSsocuId(), outstockConfigUdf);
		} else {
			cache.replace(outstockConfigUdf.getSsocuId(), outstockConfigUdf);
		}
	}

	public static void remove(Serializable id) {
		cache.remove(id);
	}

	public static void remove(Collection<? extends Serializable> idList) {
		idList.forEach(id -> {
			remove(id);
		});
	}

	public static void removeBySsodId(Long ssodId) {
		cache.values().removeIf(new Predicate<OutstockConfigUdf>() {
			@Override
			public boolean test(OutstockConfigUdf outstockConfigUdf) {
				return outstockConfigUdf.getSsodId().equals(ssodId);
			}
		});
	}*/
}
