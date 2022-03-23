package org.nodes.wms.core.basedata.cache;

import org.nodes.wms.core.basedata.entity.SkuInstock;
import org.nodes.wms.core.basedata.service.ISkuInstockService;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author pengwei
 * @program WmsCore
 * @description 物品入库设置缓存类
 * @since 2020-12-11
 */
public class SkuInstockCache {

	static ISkuInstockService skuInstockService;

	static Map<Serializable, SkuInstock> cache = new HashMap<>();


	static {
		skuInstockService = SpringUtil.getBean(ISkuInstockService.class);

		skuInstockService.list().forEach(skuInstock -> {
			cache.put(skuInstock.getWsiId(), skuInstock);
		});
	}

/*	public static SkuInstock getById(Serializable ssiId) {
		return cache.getOrDefault(ssiId, null);
	}

	public static List<SkuInstock> list(){
		return new ArrayList<>(cache.values());
	}

	public static void saveOrUpdate(SkuInstock skuInstock) {
		SkuInstock findObj = getById(skuInstock.getWsiId());
		if (Func.isEmpty(findObj)) {
			cache.put(skuInstock.getWsiId(), skuInstock);
		} else {
			cache.replace(skuInstock.getWsiId(), skuInstock);
		}
	}
	public static void saveOrUpdateBatch(Collection<SkuInstock> skuInstocks){
		skuInstocks.forEach(skuInstock -> {
			saveOrUpdate(skuInstock);
		});
	}
	public static void remove(Serializable wsiId) {
		cache.remove(wsiId);
	}

	public static void remove(Collection<? extends Serializable> idList) {
		idList.forEach(id -> {
			remove(id);
		});
	}

	public static void removeBySkuId(Serializable skuId) {
		cache.values().removeIf(new Predicate<SkuInstock>() {
			@Override
			public boolean test(SkuInstock skuInstock) {
				return skuInstock.getSkuId().equals(skuId);
			}
		});
	}

	*//**
	 * 获取物品入库设置
	 *
	 * @param skuId 物品ID
	 * @param whId  库房ID
	 * @return 入库设置
	 *//*
	public static SkuInstock getOne(Serializable skuId, Serializable whId) {
		return cache.values().stream().filter(u -> {
			if (Func.isEmpty(u.getSkuId()) || Func.isEmpty(u.getWhId())){
				return false;
			}
			return u.getSkuId().equals(skuId) && u.getWhId().equals(whId);
		}).findFirst().orElse(null);
	}

	*//**
	 * 判断指定上架策略在入库设置中存在
	 *
	 * @param ssiId 上架策略ID
	 * @return 是否存在
	 *//*
	public static boolean exist(Serializable ssiId) {
		return cache.values().stream().filter(u -> {
			return Func.isNotEmpty(ssiId) && ssiId.equals(u.getSsiId());
		}).count() > 0;
	}*/
}
