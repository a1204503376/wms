package org.nodes.wms.core.basedata.cache;


import org.nodes.wms.core.basedata.dto.SkuUmDTO;
import org.nodes.wms.core.basedata.entity.SkuUm;
import org.nodes.wms.core.basedata.service.ISkuUmService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static org.nodes.core.base.cache.CacheNames.NODES_FLASH;

/**
 * 计量单位
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class SkuUmCache {
	/**
	 * 计量单位编码：文件导入使用
	 */
	static final String SKUUM_CACHE_CODE_IMPORT = "skuum:code:import";

	static Map<Long, SkuUm> skuUmMap = new HashMap<>();

	static ISkuUmService skuUmService;

	static {
		skuUmService = SpringUtil.getBean(ISkuUmService.class);

		skuUmService.list().forEach(skuUm -> {
			skuUmMap.put(skuUm.getWsuId(), skuUm);
		});
	}

	/**
	 * 获得计量单位信息
	 *
	 * @param id
	 * @return
	 */
	/*public static SkuUm getById(Long id) {
		return skuUmMap.getOrDefault(id, null);
	}

	public static List<SkuUm> list() {
		return new ArrayList(skuUmMap.values());
	}
	*//**
	 * 根据umCode获得计量单位信息
	 *
	 * @param umCode
	 * @return
	 *//*
	public static SkuUm getByCode(String umCode) {
		return skuUmMap.values().stream().filter(skuUm -> {
			return skuUm.getWsuCode().equals(umCode);
		}).findFirst().orElse(null);
	}

	*//**
	 * 根据umCode获得计量单位信息
	 *
	 * @param codeList 编码集合
	 * @return
	 *//*
	public static List<SkuUm> listByCode(List<String> codeList) {
		return skuUmMap.values().stream().filter(skuUm -> {
			return codeList.contains(skuUm.getWsuCode());
		}).collect(Collectors.toList());
	}

	*//**
	 * 获取计量单位列表
	 *
	 * @param wsuName 计量单位名称
	 * @return 计量单位列表
	 *//*
	public static List<SkuUm> listByName(String wsuName) {
		return skuUmMap.values().stream().filter(skuUm -> {
			return skuUm.getWsuName().equals(wsuName);
		}).collect(Collectors.toList());
	}

	*//**
	 * 保存 或 更新 计量单位
	 *
	 * @param skuUm 计量单位
	 *//*
	public static void saveOrUpdate(SkuUm skuUm) {
		SkuUm findObj = getById(skuUm.getWsuId());
		if (Func.isEmpty(findObj)) {
			skuUmMap.put(skuUm.getWsuId(), skuUm);
		} else {
			skuUmMap.replace(skuUm.getWsuId(), skuUm);
		}
	}

	*//**
	 * 删除计量单位
	 *
	 * @param id 主键ID
	 *//*
	public static void removeById(Serializable id) {
		skuUmMap.remove(id);
	}

	public static void removeByIds(Collection<? extends Serializable> idList) {
		idList.forEach(id->removeById(id));
	}*/
//
//	/**
//	 * 文件导入缓存：存储缓存
//	 *
//	 * @param skuUmDTO 计量单位信息
//	 */
//	public static void putDTO(String code, SkuUmDTO skuUmDTO) {
//		CacheUtil.put(NODES_BASEDATA_FLASH, SKUUM_CACHE_CODE_IMPORT, code, skuUmDTO);
//	}
//
//	/**
//	 * 文件导入缓存：获得计量单位信息
//	 *
//	 * @param code 计量编码
//	 * @return 计量单位信息
//	 */
//	public static SkuUmDTO getDTO(String code) {
//		return CacheUtil.get(NODES_BASEDATA_FLASH, SKUUM_CACHE_CODE_IMPORT, code, () -> {
//			return null;
//		});
//	}
//
//	/**
//	 * 文件导入缓存：从缓存中移除
//	 *
//	 * @param code 计量编码
//	 */
//	public static void removeDTO(String code) {
//		CacheUtil.evict(NODES_BASEDATA_FLASH, SKUUM_CACHE_CODE_IMPORT, code);
//	}

	/**
	 * 文件导入缓存：存储到临时缓存
	 *
	 * @param code  计量单位编码
	 * @param skuUmDTO 计量单位信息
	 */
	public static void put(String code, SkuUmDTO skuUmDTO) {
		CacheUtil.put(NODES_FLASH, SKUUM_CACHE_CODE_IMPORT, code, skuUmDTO);
	}

	/**
	 * 文件导入缓存：获得计量单位信息
	 *
	 * @param code 计量单位编码
	 * @return 计量单位信息
	 */
	public static SkuUmDTO get(String code) {
		return CacheUtil.get(NODES_FLASH, SKUUM_CACHE_CODE_IMPORT, code, () -> {
			return null;
		});
	}
	/**
	 * 文件导入缓存：从缓存中移除
	 *
	 * @param code 计量单位编码
	 */
	public static void remove(String code) {
		CacheUtil.evict(NODES_FLASH, SKUUM_CACHE_CODE_IMPORT, code);
	}
}
