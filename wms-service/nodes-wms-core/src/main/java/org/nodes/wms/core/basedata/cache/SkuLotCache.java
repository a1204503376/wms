package org.nodes.wms.core.basedata.cache;

import org.nodes.wms.core.basedata.dto.SkuLotDTO;
import org.nodes.wms.core.basedata.entity.SkuLot;
import org.nodes.wms.core.basedata.service.ISkuLotService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.nodes.core.base.cache.CacheNames.NODES_FLASH;

/**
 * 物品批属性
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class SkuLotCache {
	/**
	 * 批属性编码：文件导入使用
	 */
	static final String SKULOT_CACHE_CODE_IMPORT = "skulot:code:import";

	static Map<Long, SkuLot> skuLotMap = new HashMap<>();

	private static ISkuLotService skuLotService;

	private static final Map<Serializable, SkuLot> cache = new HashMap();

	static {
		skuLotService = SpringUtil.getBean(ISkuLotService.class);
		skuLotService.list().forEach(skuLot -> {
			cache.put(skuLot.getWslId(), skuLot);
		});
	}

	/*public static SkuLot getById(Serializable id) {
		SkuLot skuLot = cache.getOrDefault(id, null);
		if (Func.isEmpty(skuLot)) {
			skuLot = skuLotService.getById(id);
			if (Func.isNotEmpty(skuLot)) {
				cache.put(id, skuLot);
			}
		}
		return skuLot;
	}

	public static void remove(Serializable id) {
		cache.remove(id);
	}

	public static void removeByIds(Collection<? extends Serializable> idList) {
		idList.forEach(id->cache.remove(id));
	}

	public static void saveOrUpdate(SkuLot entity) {
		SkuLot skuLot = getById(entity.getWslId());
		if (Func.isEmpty(skuLot)) {
			cache.put(skuLot.getWslId(), skuLot);
		} else {
			cache.replace(skuLot.getWslId(), skuLot);
		}
	}

	*//**
	 * 根据skuLotCode获得批属性信息
	 *
	 * @param skuLotCode
	 * @return
	 *//*
	public static SkuLot getByCode(String skuLotCode) {
		return cache.values().stream().filter(skuLot -> {
			return skuLot.getSkuLotCode().equals(skuLotCode);
		}).findFirst().orElse(null);
	}*/
	/**
	 * 文件导入缓存：存储到临时缓存
	 *
	 * @param code  批属性编码
	 * @param skuLotDTO 批属性信息
	 */
	public static void put(String code, SkuLotDTO skuLotDTO) {
		CacheUtil.put(NODES_FLASH, SKULOT_CACHE_CODE_IMPORT, code, skuLotDTO);
	}

	/**
	 * 文件导入缓存：获得批属性信息
	 *
	 * @param code 批属性编码
	 * @return 计量单位信息
	 */
	public static SkuLotDTO get(String code) {
		return CacheUtil.get(NODES_FLASH, SKULOT_CACHE_CODE_IMPORT, code, () -> {
			return null;
		});
	}
	/**
	 * 文件导入缓存：从缓存中移除
	 *
	 * @param code 批属性编码
	 */
	public static void remove(String code) {
		CacheUtil.evict(NODES_FLASH, SKULOT_CACHE_CODE_IMPORT, code);
	}
}
