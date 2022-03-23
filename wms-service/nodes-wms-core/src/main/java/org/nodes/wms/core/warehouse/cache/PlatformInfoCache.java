package org.nodes.wms.core.warehouse.cache;

import org.nodes.wms.core.warehouse.dto.PlatformInfoDTO;
import org.nodes.wms.core.warehouse.entity.PlatformInfo;
import org.nodes.wms.core.warehouse.service.IPlatformInfoService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.HashMap;
import java.util.Map;

import static org.nodes.core.base.cache.CacheNames.NODES_FLASH;

/**
 * 月台缓存
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class PlatformInfoCache {
	/**
	 * 容器编码：文件导入使用
	 */
	static final String PLATFORMINFO_CACHE_CODE_IMPORT = "platforminfo:code:import";

	public static final String PLATFORM_CACHE = "wms.platform";
	//月台id
	private static final String PLATFORMINFO_CACHE_ID = "platforminfo:id";
	static Map<Long, PlatformInfo> cache = new HashMap<>();

	private static IPlatformInfoService platformInfoService;

	static {
		platformInfoService = SpringUtil.getBean(IPlatformInfoService.class);
		platformInfoService.list().forEach(platformInfo -> {
			cache.put(platformInfo.getSpiId(), platformInfo);
		});
	}

	/**
	 * 获得月台信息
	 *
	 * @param id
	 * @return
	 */
	public static PlatformInfo getById(Long id) {
		return CacheUtil.get(PLATFORM_CACHE, PLATFORMINFO_CACHE_ID, id, () -> platformInfoService.getById(id));
	}

	/**
	 * 文件导入缓存：存储到临时缓存
	 *
	 * @param code            月台编码
	 * @param platformInfoDTO 月台信息
	 */
	public static void put(String code, PlatformInfoDTO platformInfoDTO) {
		CacheUtil.put(NODES_FLASH, PLATFORMINFO_CACHE_CODE_IMPORT, code, platformInfoDTO);
	}

	/**
	 * 文件导入缓存：获得容器信息
	 *
	 * @param code 容器编码
	 * @return 容器信息
	 */
	public static PlatformInfoDTO get(String code) {
		return CacheUtil.get(NODES_FLASH, PLATFORMINFO_CACHE_CODE_IMPORT, code, () -> {
			return null;
		});
	}

	/**
	 * 文件导入缓存：从缓存中移除
	 *
	 * @param code 容器编码
	 */
	public static void remove(String code) {
		CacheUtil.evict(NODES_FLASH, PLATFORMINFO_CACHE_CODE_IMPORT, code);
	}

	/**
	 * 获得容器信息
	 *
	 * @param code 容器编码
	 * @return 容器信息
	 */
	/*public static PlatformInfo getByCode(String code) {
		return cache.values().stream().filter(platformInfo -> {
			return platformInfo.getPlatformCode().equals(code);
		}).findFirst().orElse(null);
	}

	*//**
	 * 保存 或 更新 容器
	 *
	 * @param platformInfo 容器
	 *//*
	public static void saveOrUpdate(PlatformInfo platformInfo) {
		PlatformInfo findObj = getById(platformInfo.getSpiId());
		if (Func.isEmpty(findObj)) {
			cache.put(platformInfo.getSpiId(), platformInfo);
		} else {
			cache.replace(platformInfo.getSpiId(), platformInfo);
		}
	}*/
}
