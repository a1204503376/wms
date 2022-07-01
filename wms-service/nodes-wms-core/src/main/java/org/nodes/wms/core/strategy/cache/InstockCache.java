package org.nodes.wms.core.strategy.cache;

import org.nodes.wms.core.strategy.service.IInstockService;


/**
 * 上架策略缓存
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class InstockCache {

	private static IInstockService instockService;

	/*static final Map<Serializable, Instock> cache = new HashMap<>();

	static {
		instockService = SpringUtil.getBean(IInstockService.class);
		instockService.list().forEach(instock -> {
			cache.put(instock.getSsiId(), instock);
		});
	}

	*//**
	 * 添加或修改策略信息
	 *
	 * @param instock
	 *//*
	public static void saveOrUpdate(Instock instock) {
		Instock findObj = getById(instock.getSsiId());
		if (Func.isEmpty(findObj)) {
			cache.put(instock.getSsiId(), instock);
		} else {
			cache.replace(instock.getSsiId(), instock);
		}
	}

	public static void remove(Serializable ssiId) {
		cache.remove(ssiId);
	}

	*//**
	 * 获得上架策略信息
	 *
	 * @param id
	 * @return
	 *//*
	public static Instock getById(Serializable id) {
		return cache.getOrDefault(id, null);
	}

	public static List<Instock> list() {
		return new ArrayList(cache.values());
	}

	public static List<Instock> listByWhId(Long whId) {
		return list().stream().filter(u -> whId.equals(u.getWhId())).collect(Collectors.toList());
	}*/
}
