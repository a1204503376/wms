package org.nodes.wms.core.basedata.cache;

import org.nodes.wms.core.basedata.dto.SkuTypeDTO;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.nodes.wms.core.basedata.service.ISkuTypeService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.SpringUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.nodes.core.base.cache.CacheNames.NODES_FLASH;

/**
 * 物品分类
 *
 * @author chenhz
 * @program 类名称
 * @description 类名称描述
 * @create 20191216
 */
public class SkuTypeCache {

	final static Map<Serializable, SkuType> cache = new HashMap<>();
	private static final String SKUTYPE_CODE_IMPORT = "skutype:code:import";
	private static ISkuTypeService skuTypeService;

	static {
		skuTypeService = SpringUtil.getBean(ISkuTypeService.class);
	}

	/*public static SkuType getById(Serializable id) {
		SkuType entity = cache.getOrDefault(id, null);
		if (Func.isEmpty(entity)) {
			entity = skuTypeService.getById(id);
			if (Func.isNotEmpty(entity)) {
				cache.put(id, entity);
			}
		}
		return entity;
	}*/
	public static void remove(String code) {
		CacheUtil.evict(NODES_FLASH, SKUTYPE_CODE_IMPORT, code);
	}

	public static void put(String code, SkuTypeDTO skuTypeDTO) {
		CacheUtil.put(NODES_FLASH, SKUTYPE_CODE_IMPORT, code, skuTypeDTO);
	}
	public static SkuTypeDTO get(String code) {
		return CacheUtil.get(NODES_FLASH, SKUTYPE_CODE_IMPORT, code, () -> {
			return null;
		});
	}
	/*public static void saveOrUpdate(SkuType skuType) {
		SkuType findObj = getById(skuType.getSkuTypeId());
		if (Func.isEmpty(findObj)) {
			cache.put(skuType.getSkuTypeId(), skuType);
		} else {
			cache.replace(skuType.getSkuTypeId(), skuType);
		}
	}*/
}
