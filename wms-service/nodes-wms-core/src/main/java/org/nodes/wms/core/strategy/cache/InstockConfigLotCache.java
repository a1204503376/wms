package org.nodes.wms.core.strategy.cache;

/**
 * @author pengwei
 * @program WmsCore
 * @description 上架策略批属性设定缓存类
 * @since 2020-12-11
 */
public class InstockConfigLotCache {

	/*static Map<Long, InstockConfigLot> cache = new HashMap<>();

	static {
		IInstockConfigLotService instockConfigLotService = SpringUtil.getBean(IInstockConfigLotService.class);

		instockConfigLotService.list().forEach(instockConfigLot -> {
			cache.put(instockConfigLot.getSsiclId(), instockConfigLot);
		});
	}

	public static List<InstockConfigLot> listBySsidId(Long ssidId) {
		return cache.values().stream().filter(u -> {
			return u.getSsidId().equals(ssidId);
		}).collect(Collectors.toList());
	}

	public static InstockConfigLot getById(Long ssiclId) {
		return cache.getOrDefault(ssiclId, null);
	}

	public static void saveOrUpdate(InstockConfigLot instockConfigLot) {
		InstockConfigLot findObj = getById(instockConfigLot.getSsiclId());
		if (Func.isEmpty(findObj)) {
			cache.put(instockConfigLot.getSsiclId(), instockConfigLot);
		} else {
			cache.replace(instockConfigLot.getSsiclId(), instockConfigLot);
		}
	}

	public static void remove(Serializable id) {
		cache.remove(id);
	}

	public static void remove(Collection<? extends Serializable> idList) {
		idList.forEach(id->{
			remove(id);
		});
	}

	public static void removeBySsidId(Long ssidId) {
		cache.values().removeIf(new Predicate<InstockConfigLot>() {
			@Override
			public boolean test(InstockConfigLot instockConfigLot) {
				return instockConfigLot.getSsidId().equals(ssidId);
			}
		});
	}*/
}
