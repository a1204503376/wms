package org.nodes.wms.core.strategy.cache;

import org.nodes.wms.core.strategy.entity.OutstockConfig;
import org.nodes.wms.core.strategy.service.IOutstockConfigService;
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
 * @description 分配策略执行条件缓存类
 * @since 2020-12-08
 */
public class OutstockConfigCache {

	/*static Map<Long, OutstockConfig> cache = new HashMap<>();

	static {
		IOutstockConfigService outstockConfigService = SpringUtil.getBean(IOutstockConfigService.class);

		outstockConfigService.list().forEach(outstockConfig -> {
			cache.put(outstockConfig.getSsocId(), outstockConfig);
		});
	}

	public static List<OutstockConfig> listBySsodId(Long ssodId) {
		return cache.values().stream().filter(u -> {
			return u.getSsodId().equals(ssodId);
		}).collect(Collectors.toList());
	}

	public static OutstockConfig getById(Long ssocId) {
		return cache.getOrDefault(ssocId, null);
	}

	public static void removeBySsodId(Long ssodId) {
		cache.values().removeIf(new Predicate<OutstockConfig>() {
			@Override
			public boolean test(OutstockConfig outstockConfig) {
				return outstockConfig.getSsodId().equals(ssodId);
			}
		});
	}

	public static void saveOrUpdate(OutstockConfig outstockConfig) {
		OutstockConfig findObj = getById(outstockConfig.getSsocId());
		if (Func.isEmpty(findObj)) {
			cache.put(outstockConfig.getSsocId(), outstockConfig);
		} else {
			cache.replace(outstockConfig.getSsocId(), outstockConfig);
		}
	}

	public static void remove(Long ssocId) {
		cache.remove(ssocId);
	}

	public static void remove(Collection<? extends Serializable> idList) {
		idList.forEach(id -> {
			remove((Long)id);
		});
	}*/
}
