package org.nodes.wms.core.strategy.cache;

import org.nodes.wms.core.strategy.entity.RelenishmentDetail;
import org.nodes.wms.core.strategy.service.IRelenishmentDetailService;
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
 * @description 补货策略明细缓存类
 * @since 2020-12-11
 */
public class RelenishmentDetailCache {

/*	static Map<Long, RelenishmentDetail> cache = new HashMap<>();

	static {
		IRelenishmentDetailService relenishmentDetailService = SpringUtil.getBean(IRelenishmentDetailService.class);

		relenishmentDetailService.list().forEach(instockDetail -> {
			cache.put(instockDetail.getSsidId(), instockDetail);
		});
	}

	*//**
	 * 获取补货策略明细集合
	 *
	 * @param ssiId 补货策略主键ID
	 * @return 补货策略明细集合
	 *//*
	public static List<RelenishmentDetail> list(Serializable ssiId) {
		return cache.values().stream().filter(u -> {
			return u.getSsiId().equals(ssiId);
		}).collect(Collectors.toList());
	}
	*//**
	 * 获取补货策略明细集合
	 *
	 * @param ssiIdList 补货策略主键ID
	 * @return 补货策略明细集合
	 *//*
	public static List<RelenishmentDetail> listBySsiIds(List<Long> ssiIdList) {
		return cache.values().stream().filter(u->{
			return ssiIdList.contains(u.getSsiId());
		}).collect(Collectors.toList());
	}

	*//**
	 * 根据补货策略明细ID获取补货策略明细
	 *
	 * @param ssidId 补货策略明细ID
	 * @return 补货策略明细
	 *//*
	public static RelenishmentDetail getById(Long ssidId) {
		return cache.getOrDefault(ssidId, null);
	}

	*//**
	 * 修改或更新补货策略明细到缓存
	 *
	 * @param relenishmentDetail 补货策略明细
	 * @return 是否成功
	 *//*
	public static void saveOrUpdate(RelenishmentDetail relenishmentDetail) {
		RelenishmentDetail findObj = getById(relenishmentDetail.getSsidId());
		if (Func.isEmpty(findObj)) {
			cache.put(relenishmentDetail.getSsidId(), relenishmentDetail);
		} else {
			cache.replace(relenishmentDetail.getSsidId(), relenishmentDetail);
		}
	}

	*//**
	 * 从缓存中删除指定补货策略明细
	 *
	 * @param ssidId 补货策略明细ID
	 *//*
	public static void remove(Long ssidId) {
		cache.remove(ssidId);
	}

	*//**
	 * 从缓存中删除指定策略明细
	 *
	 * @param idList 补货策略明细ID集合
	 *//*
	public static void remove(Collection<? extends Serializable> idList) {
		idList.forEach(id -> {
			remove((Long) id);
		});
	}

	*//**
	 * 从缓存中删除指定策略明细
	 *
	 * @param ssiId 补货策略ID
	 *//*
	public static void removeBySsoId(Long ssiId) {
		cache.values().removeIf(new Predicate<RelenishmentDetail>() {
			@Override
			public boolean test(RelenishmentDetail relenishmentDetail) {
				return relenishmentDetail.getSsiId().equals(ssiId);
			}
		});
	}

	*//**
	 * 判断指定补货策略明细ID 在缓存中是否存在
	 *
	 * @param ssidId 补货策略明细ID
	 * @return 是否存在
	 *//*
	public static boolean exist(Long ssidId) {
		return cache.containsKey(ssidId);
	}*/
}
