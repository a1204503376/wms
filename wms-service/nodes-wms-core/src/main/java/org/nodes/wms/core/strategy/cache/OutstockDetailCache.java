package org.nodes.wms.core.strategy.cache;

import org.nodes.wms.core.strategy.entity.OutstockDetail;
import org.nodes.wms.core.strategy.service.IOutstockDetailService;
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
 * @description 分配策略明细缓存类
 * @since 2020-12-07
 */
public class OutstockDetailCache {

	/*static Map<Long, OutstockDetail> cache = new HashMap<>();

	static {
		IOutstockDetailService outstockDetailService = SpringUtil.getBean(IOutstockDetailService.class);

		outstockDetailService.list().forEach(outstockDetail -> {
			cache.put(outstockDetail.getSsodId(), outstockDetail);
		});
	}

	*//**
	 * 获取分配策略明细集合
	 *
	 * @param ssoId 分配策略主键ID
	 * @return 分配策略明细集合
	 *//*
	public static List<OutstockDetail> list(Long ssoId) {
		return cache.values().stream().filter(u -> {
			return u.getSsoId().equals(ssoId);
		}).collect(Collectors.toList());
	}

	*//**
	 * 根据分配策略明细ID获取分配策略明细
	 *
	 * @param ssodId 分配策略明细ID
	 * @return 分配策略明细
	 *//*
	public static OutstockDetail getById(Long ssodId) {
		return cache.getOrDefault(ssodId, null);
	}

	*//**
	 * 修改或更新分配策略明细到缓存
	 *
	 * @param outstockDetail 分配策略明细
	 * @return 是否成功
	 *//*
	public static void saveOrUpdate(OutstockDetail outstockDetail) {
		OutstockDetail findObj = getById(outstockDetail.getSsodId());
		if (Func.isEmpty(findObj)) {
			cache.put(outstockDetail.getSsodId(), outstockDetail);
		} else {
			cache.replace(outstockDetail.getSsodId(), outstockDetail);
		}
	}

	*//**
	 * 从缓存中删除指定策略明细
	 *
	 * @param ssodId 分配策略明细ID
	 *//*
	public static void remove(Long ssodId) {
		cache.remove(ssodId);
	}

	*//**
	 * 从缓存中删除指定策略明细
	 *
	 * @param idList 分配策略明细ID集合
	 *//*
	public static void remove(Collection<? extends Serializable> idList) {
		idList.forEach(id -> {
			remove((Long) id);
		});
	}

	*//**
	 * 从缓存中删除指定策略明细
	 *
	 * @param ssoId 分配策略ID
	 *//*
	public static void removeBySsoId(Long ssoId) {
		cache.values().removeIf(new Predicate<OutstockDetail>() {
			@Override
			public boolean test(OutstockDetail outstockDetail) {
				return outstockDetail.getSsoId().equals(ssoId);
			}
		});
	}

	*//**
	 * 判断指定分配策略明细ID 在缓存中是否存在
	 *
	 * @param ssodId 分配策略明细ID
	 * @return 是否存在
	 *//*
	public static boolean exist(Long ssodId) {
		return cache.containsKey(ssodId);
	}

	*//**
	 * 判断分配策略明细是否在缓存中存在
	 *
	 * @param ssoId        策略ID
	 * @param outstockFunc 分配策略计算代码
	 * @return 是否存在
	 *//*
	public static boolean exist(Long ssoId, String outstockFunc) {
		return cache.values().stream().filter(u -> {
			return u.getSsoId().equals(ssoId) && outstockFunc.equals(u.getOutstockFunction());
		}).count() > 0;
	}*/
}
