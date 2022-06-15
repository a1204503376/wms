package org.nodes.wms.core.basedata.cache;

import org.nodes.wms.core.basedata.dto.SkuDTO;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.io.Serializable;

import static org.nodes.core.base.cache.CacheNames.NODES_FLASH;


/**
 * 物品缓存
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class SkuCache {

	public static final String SKU_CACHE = "wms:sku";

	static final String SKU_ID = "sku:id:";

	/**
	 * 物品编码+货主ID：文件导入使用
	 */
	static final String SKU_CACHE_CODE_WOID_IMPORT = "sku:code,woid:import";


	static ISkuService skuService;

	static {
		skuService = SpringUtil.getBean(ISkuService.class);
	}

	/**
	 * 获得物品信息
	 *
	 * @param id 主键ID
	 * @return 物品信息
	 */
	public static Sku getById(Serializable id) {
		return CacheUtil.get(SKU_CACHE, SKU_ID, id, ()->skuService.getById(id));
	}

	public static Sku getValid(Serializable id) {
		Sku sku = getById(id);
		if (Func.isEmpty(sku)) {
			throw new ServiceException("物品(id=" + id + ") 不存在或已删除！");
		}
		return sku;
	}

	/**
	 * 根据物品编码和货主id获得物品信息
	 *
	 * @param skuCode
	 * @param woId
	 * @return
	 *//*
	public static Sku getOne(String skuCode, Long woId) {
		return cache.values().stream().filter(sku -> {
			return sku.getSkuCode().equals(skuCode) && sku.getWoId().equals(woId);
		}).findFirst().orElse(null);
	}

	public static Sku getSku(String skuName, Long woId) {
		return cache.values().stream().filter(sku -> {
			return sku.getSkuName().equals(skuName) && sku.getWoId().equals(woId);
		}).findFirst().orElse(null);
	}

	*//**
	 * 获取物品列表
	 *
	 * @return 物品列表
	 *//*
	public static List<Sku> list() {
		return new ArrayList<>(cache.values());
	}

	public static List<Sku> listByIds(Collection<? extends Serializable> idList) {
		return cache.values().stream().filter(u -> idList.contains(u.getSkuId())).collect(Collectors.toList());
	}

	*//**
	 * 根据物品编码获取物品列表
	 *
	 * @param skuCode 物品编码
	 * @return 物品列表
	 *//*
	public static List<Sku> listByCode(String skuCode) {
		return cache.values().stream().filter(sku -> {
			return sku.getSkuCode().equals(skuCode);
		}).collect(Collectors.toList());
	}

	*//**
	 * 根据物品编码获取物品列表
	 *
	 * @param skuCodeList 物品编码集合
	 * @return 物品列表
	 *//*
	public static List<Sku> listByCode(List<String> skuCodeList) {
		if (Func.isEmpty(skuCodeList)) {
			return new ArrayList<>();
		} else {
			return cache.values().stream().filter(sku -> {
				return skuCodeList.contains(sku.getSkuCode());
			}).collect(Collectors.toList());
		}
	}

	*//**
	 * 根据货主ID获取物品列表
	 *
	 * @param woId 货主ID
	 * @return 物品列表
	 *//*
	public static List<Sku> listByWoId(Long woId) {
		return cache.values().stream().filter(sku -> {
			return sku.getWoId().equals(woId);
		}).collect(Collectors.toList());
	}

	*//**
	 * 根据包装ID获取物品列表
	 *
	 * @param wspId 包装ID
	 * @return 物品列表
	 *//*
	public static List<Sku> listByWspId(Long wspId) {
		return cache.values().stream().filter(sku -> {
			return sku.getWspId().equals(wspId);
		}).collect(Collectors.toList());
	}*/

	/**
	 * 文件导入缓存：获得货主信息
	 *
	 * @param code 货主编码
	 * @return 货主信息
	 */
	public static SkuDTO get(String code) {
		return CacheUtil.get(NODES_FLASH, SKU_CACHE_CODE_WOID_IMPORT, code, () -> {
			return null;
		}, true);
	}

	/**
	 * 文件导入缓存：从缓存中移除
	 *
	 * @param code 货主编码
	 */
	public static void remove(String code) {
		CacheUtil.evict(NODES_FLASH, SKU_CACHE_CODE_WOID_IMPORT, code);
	}

	public static void put(String code, SkuDTO skuDTO) {
		CacheUtil.put(NODES_FLASH, SKU_CACHE_CODE_WOID_IMPORT, code, skuDTO);
	}

	/**
	 * 导入 获得物品信息
	 *
	 * @param code 物品编码+货主编码
	 * @return 物品
	 */
	public static SkuDTO getDTO(String code) {
		return CacheUtil.get(SKU_CACHE, SKU_CACHE_CODE_WOID_IMPORT, code, () -> {
			return null;
		});
	}

	/**
	 * 导入 从缓存中移除
	 *
	 * @param code 编码
	 */
	public static void removeDTO(String code) {
		CacheUtil.evict(SKU_CACHE, SKU_CACHE_CODE_WOID_IMPORT, code);
	}
}
