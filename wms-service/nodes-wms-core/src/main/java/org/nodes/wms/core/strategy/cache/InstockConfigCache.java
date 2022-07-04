package org.nodes.wms.core.strategy.cache;

/**
 * @author pengwei
 * @program WmsCore
 * @description 上架策略执行条件缓存类
 * @since 2020-12-11
 */
public class InstockConfigCache {

	/*static Map<Long, InstockConfig> cache = new HashMap<>();

	static {
		IInstockConfigService instockConfigService = SpringUtil.getBean(IInstockConfigService.class);

		instockConfigService.list().forEach(instockConfig -> {
			cache.put(instockConfig.getSsicId(), instockConfig);
		});
	}

	public static List<InstockConfig> listByssidId(Long ssidId) {
		return cache.values().stream().filter(u -> {
			return u.getSsidId().equals(ssidId);
		}).collect(Collectors.toList());
	}

	public static InstockConfig getById(Long ssicId) {
		return cache.getOrDefault(ssicId, null);
	}

	public static void removeBySsodId(Long ssodId) {
		cache.values().removeIf(new Predicate<InstockConfig>() {
			@Override
			public boolean test(InstockConfig instockConfig) {
				return instockConfig.getSsicId().equals(ssodId);
			}
		});
	}

	public static void saveOrUpdate(InstockConfig instockConfig) {
		InstockConfig findObj = getById(instockConfig.getSsicId());
		if (Func.isEmpty(findObj)) {
			cache.put(instockConfig.getSsicId(), instockConfig);
		} else {
			cache.replace(instockConfig.getSsicId(), instockConfig);
		}
	}

	public static void remove(Long ssicId) {
		cache.remove(ssicId);
	}

	public static void remove(Collection<? extends Serializable> idList) {
		idList.forEach(id -> {
			remove((Long)id);
		});
	}*/
}
