package org.nodes.wms.core.strategy.cache;

import org.nodes.wms.core.strategy.entity.Relenishment;
import org.nodes.wms.core.strategy.service.IRelenishmentService;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 补货策略缓存
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class RelenishmentCache {

	/*private static IRelenishmentService relenishmentService;

	static final Map<Serializable, Relenishment> cache = new HashMap<>();

	static {
		relenishmentService = SpringUtil.getBean(IRelenishmentService.class);
		relenishmentService.list().forEach(relenishment -> {
			cache.put(relenishment.getSsiId(), relenishment);
		});
	}

	*//**
	 * 获得补货策略信息
	 *
	 * @param id
	 * @return
	 *//*
	public static Relenishment getById(Serializable id) {
		return cache.getOrDefault(id, null);
	}

	public static List<Relenishment> list() {
		return new ArrayList(cache.values());
	}

	public static List<Relenishment> listByWhId(Long whId) {
		return list().stream().filter(u -> whId.equals(u.getWhId())).collect(Collectors.toList());
	}
	public static void saveOrUpdate(Relenishment relenishment) {
		Relenishment findObj = getById(relenishment.getSsiId());
		if (Func.isEmpty(findObj)) {
			cache.put(relenishment.getSsiId(), relenishment);
		} else {
			cache.replace(relenishment.getSsiId(), relenishment);
		}
	}*/
}
