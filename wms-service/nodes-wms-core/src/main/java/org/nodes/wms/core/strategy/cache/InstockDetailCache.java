package org.nodes.wms.core.strategy.cache;

/**
 * @author pengwei
 * @program WmsCore
 * @description 上架策略明细缓存类
 * @since 2020-12-11
 */
public class InstockDetailCache {

/*	static Map<Long, InstockDetail> cache = new HashMap<>();

	static {
		IInstockDetailService instockDetailService = SpringUtil.getBean(IInstockDetailService.class);

		instockDetailService.list().forEach(instockDetail -> {
			cache.put(instockDetail.getSsidId(), instockDetail);
		});
	}

	*//**
	 * 获取上架策略明细集合
	 *
	 * @param ssiId 上架策略主键ID
	 * @return 上架策略明细集合
	 *//*
	public static List<InstockDetail> list(Serializable ssiId) {
		return cache.values().stream().filter(u -> {
			return u.getSsiId().equals(ssiId);
		}).collect(Collectors.toList());
	}
	*//**
	 * 获取上架策略明细集合
	 *
	 * @param ssiIdList 上架策略主键ID
	 * @return 上架策略明细集合
	 *//*
	public static List<InstockDetail> listBySsiIds(List<Long> ssiIdList) {
		return cache.values().stream().filter(u->{
			return ssiIdList.contains(u.getSsiId());
		}).collect(Collectors.toList());
	}

	*//**
	 * 根据上架策略明细ID获取上架策略明细
	 *
	 * @param ssidId 上架策略明细ID
	 * @return 上架策略明细
	 *//*
	public static InstockDetail getById(Long ssidId) {
		return cache.getOrDefault(ssidId, null);
	}

	*//**
	 * 修改或更新上架策略明细到缓存
	 *
	 * @param instockDetail 上架策略明细
	 * @return 是否成功
	 *//*
	public static void saveOrUpdate(InstockDetail instockDetail) {
		InstockDetail findObj = getById(instockDetail.getSsidId());
		if (Func.isEmpty(findObj)) {
			cache.put(instockDetail.getSsidId(), instockDetail);
		} else {
			cache.replace(instockDetail.getSsidId(), instockDetail);
		}
	}

	*//**
	 * 从缓存中删除指定上架策略明细
	 *
	 * @param ssidId 上架策略明细ID
	 *//*
	public static void remove(Long ssidId) {
		cache.remove(ssidId);
	}

	*//**
	 * 从缓存中删除指定策略明细
	 *
	 * @param idList 上架策略明细ID集合
	 *//*
	public static void remove(Collection<? extends Serializable> idList) {
		idList.forEach(id -> {
			remove((Long) id);
		});
	}

	*//**
	 * 从缓存中删除指定策略明细
	 *
	 * @param ssiId 上架策略ID
	 *//*
	public static void removeBySsoId(Long ssiId) {
		cache.values().removeIf(new Predicate<InstockDetail>() {
			@Override
			public boolean test(InstockDetail instockDetail) {
				return instockDetail.getSsiId().equals(ssiId);
			}
		});
	}

	*//**
	 * 判断指定上架策略明细ID 在缓存中是否存在
	 *
	 * @param ssidId 上架策略明细ID
	 * @return 是否存在
	 *//*
	public static boolean exist(Long ssidId) {
		return cache.containsKey(ssidId);
	}*/
}
