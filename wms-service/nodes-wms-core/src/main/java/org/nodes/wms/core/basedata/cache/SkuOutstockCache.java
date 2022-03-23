package org.nodes.wms.core.basedata.cache;

import org.nodes.wms.core.basedata.entity.SkuOutstock;
import org.nodes.wms.core.basedata.service.ISkuOutstockService;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author pengwei
 * @program WmsCore
 * @description 物品出库设置缓存类
 * @since 2020-12-07
 */
public class SkuOutstockCache {

	static ISkuOutstockService skuOutstockService;

	static Map<Serializable, SkuOutstock> cache = new HashMap<>();


	static {
		skuOutstockService = SpringUtil.getBean(ISkuOutstockService.class);

		skuOutstockService.list().forEach(skuOutstock -> {
			cache.put(skuOutstock.getWsoId(), skuOutstock);
		});
	}

	/*public static SkuOutstock getById(Serializable wsoId) {
		return cache.getOrDefault(wsoId, null);
	}

	public static void saveOrUpdateBatch(Collection<SkuOutstock> skuOutstocks){
		skuOutstocks.forEach(skuOutstock -> {
			saveOrUpdate(skuOutstock);
		});
	}
	public static void saveOrUpdate(SkuOutstock skuOutstock) {
		SkuOutstock findObj = getById(skuOutstock.getWsoId());
		if (Func.isEmpty(findObj)) {
			cache.put(skuOutstock.getWsoId(), skuOutstock);
		} else {
			cache.replace(skuOutstock.getWsoId(), skuOutstock);
		}
	}

	public static void remove(Serializable wsoId) {
		cache.remove(wsoId);
	}

	public static void remove(Collection<? extends Serializable> idList) {
		idList.forEach(id -> {
			remove(id);
		});
	}

	public static void removeBySkuId(Long skuId) {
		cache.values().removeIf(new Predicate<SkuOutstock>() {
			@Override
			public boolean test(SkuOutstock skuOutstock) {
				return skuOutstock.getSkuId().equals(skuId);
			}
		});
	}

	*//**
	 * 获取物品出库设置
	 *
	 * @param skuId 物品ID
	 * @param whId  库房ID
	 * @return 出库设置
	 *//*
	public static SkuOutstock getOne(Long skuId, Long whId) {
		return cache.values().stream().filter(u -> {
			if (Func.isEmpty(u.getSkuId()) || Func.isEmpty(u.getWhId())) {
				return false;
			}
			return u.getSkuId().equals(skuId) && u.getWhId().equals(whId);
		}).findFirst().orElse(null);
	}

	*//**
	 * 判断指定分配策略在出库设置中存在
	 *
	 * @param ssoId 分配策略ID
	 * @return 是否存在
	 *//*
	public static boolean exist(Long ssoId) {
		return cache.values().stream().filter(u -> {
			return Func.isNotEmpty(ssoId) && ssoId.equals(u.getSsoId());
		}).count() > 0;
	}

	public static List<SkuOutstock> list(){
		return new ArrayList(cache.values());
	}*/
}
