package org.nodes.wms.core.basedata.cache;

import org.nodes.wms.core.basedata.dto.SkuLotValDTO;
import org.nodes.wms.core.basedata.entity.SkuLotVal;
import org.nodes.wms.core.basedata.service.ISkuLotValService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.nodes.core.base.cache.CacheNames.NODES_FLASH;

/**
 * 批属性验证
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class SkuLotValCache {
	/**
	 * 批属性编码：文件导入使用
	 */
	static final String SKULOTVAL_CACHE_CODE_IMPORT = "skulotVal:code:import";


	final static Map<Serializable, SkuLotVal> cache = new HashMap<>();

	static ISkuLotValService skuLotValService;

	static {
		skuLotValService = SpringUtil.getBean(ISkuLotValService.class);
		skuLotValService.list().forEach(skuLotVal -> {
			cache.put(skuLotVal.getWslvId(), skuLotVal);
		});
	}

	/*public static SkuLotVal getById(Serializable id) {
		SkuLotVal skuLotVal = cache.getOrDefault(id, null);
		if (Func.isEmpty(skuLotVal)) {
			skuLotVal = skuLotValService.getById(id);
			cache.put(id, skuLotVal);
		}
		return skuLotVal;
	}
	public static void saveOrUpdate(SkuLotVal entity) {
		SkuLotVal skuLotVal = getById(entity.getWslvId());
		if (Func.isEmpty(skuLotVal)) {
			cache.put(skuLotVal.getWslvId(), skuLotVal);
		} else {
			cache.replace(skuLotVal.getWslvId(), skuLotVal);
		}
	}
	*//**
	 * 根据skuLotValName获得批属性验证信息
	 *
	 * @param skuLotValName
	 * @return
	 *//*
	public static SkuLotVal getByName(String skuLotValName) {
		return cache.values().stream().filter(skuLotVal -> {
			return skuLotVal.getSkuLotValName().equals(skuLotValName);
		}).findFirst().orElse(null);
	}*/
	/**
	 * 文件导入缓存：存储到临时缓存
	 *
	 * @param name  批属性验证名称
	 * @param skuLotValDTO 批属性验证信息
	 */
	public static void put(String name, SkuLotValDTO skuLotValDTO) {
		CacheUtil.put(NODES_FLASH, SKULOTVAL_CACHE_CODE_IMPORT, name, skuLotValDTO);
	}

	/**
	 * 文件导入缓存：获得批属性信息
	 *
	 * @param name 批属性验证名称
	 * @return 计量单位信息
	 */
	public static SkuLotValDTO get(String name) {
		return CacheUtil.get(NODES_FLASH, SKULOTVAL_CACHE_CODE_IMPORT, name, () -> {
			return null;
		});
	}
	/**
	 * 文件导入缓存：从缓存中移除
	 *
	 * @param name 批属性验证名称
	 */
	public static void remove(String name) {
		CacheUtil.evict(NODES_FLASH, SKULOTVAL_CACHE_CODE_IMPORT, name);
	}
}
