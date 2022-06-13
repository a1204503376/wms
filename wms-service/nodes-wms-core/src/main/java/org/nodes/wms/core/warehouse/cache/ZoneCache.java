package org.nodes.wms.core.warehouse.cache;

import org.nodes.wms.core.warehouse.dto.ZoneDTO;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.HashMap;
import java.util.Map;

import static org.nodes.core.base.cache.CacheNames.NODES_FLASH;

/**
 * 库区缓存
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class ZoneCache {

	public static final String ZONE_CACHE = "wms.zone";
	/**
	 * 库区编码：文件导入使用
	 */
	static final String ZONE_CACHE_CODE_IMPORT = "zone:code:import";

	static Map<Long, Zone> zoneMap = new HashMap<>();

	static IZoneService zoneService;

	static {
		zoneService = SpringUtil.getBean(IZoneService.class);

		zoneService.list().forEach(zone -> {
			zoneMap.put(zone.getZoneId(), zone);
		});
	}

	/**
	 * 获得库区信息
	 *
	 * @param id
	 * @return
	 */
	/*public static Zone getById(Long id) {
		return zoneMap.getOrDefault(id, null);
	}

	*//**
	 * 移除库区缓存
	 *
	 * @param id 库区ID
	 *//*
	public static void removeById(Long id) {
		zoneMap.remove(id);
	}

	*//**
	 * 根据code获得库区信息
	 *
	 * @param code
	 * @return
	 *//*
	public static Zone getByCode(String code) {
		return zoneMap.values().stream().filter(zone -> {
			return zone.getZoneCode().equals(code);
		}).findFirst().orElse(null);
	}

	*//**
	 * 获取库区列表
	 *
	 * @return 库区列表
	 *//*
	public static List<Zone> list() {
		return new ArrayList<>(zoneMap.values());
	}

	public static List<Zone> listByWhId(Long whId) {
		return list().stream().filter(u -> u.getWhId().equals(whId)).collect(Collectors.toList());
	}

	*//**
	 * 新增 或 更新 库区
	 *
	 * @param zone 库区
	 *//*
	public static void saveOrUpdate(Zone zone) {
		Zone findObj = getById(zone.getZoneId());
		if (Func.isEmpty(findObj)) {
			zoneMap.put(zone.getZoneId(), zone);
		} else {
			zoneMap.replace(zone.getZoneId(), zone);
		}
	}*/

	/**
	 * 文件导入缓存：存储缓存
	 *
	 * @param zoneDTO 库区信息
	 */
	public static void putDTO(String code, ZoneDTO zoneDTO) {
		CacheUtil.put(ZONE_CACHE, ZONE_CACHE_CODE_IMPORT, code, zoneDTO);
	}

	/**
	 * 文件导入缓存：从缓存中移除
	 *
	 * @param code 库区编码
	 */
	public static void removeDTO(String code) {
		CacheUtil.evict(ZONE_CACHE, ZONE_CACHE_CODE_IMPORT, code);
	}

	/**
	 * 文件导入缓存：获得分类信息
	 *
	 * @param code 库区编码
	 * @return 库区信息
	 */
	public static ZoneDTO getDTO(String code) {
		return CacheUtil.get(ZONE_CACHE, ZONE_CACHE_CODE_IMPORT, code, () -> {
			return null;
		});
	}

	/**
	 * 文件导入缓存：存储到临时缓存
	 *
	 * @param code    库区编码
	 * @param zoneDTO 库区信息
	 */
	public static void put(String code, ZoneDTO zoneDTO) {
		CacheUtil.put(NODES_FLASH, ZONE_CACHE_CODE_IMPORT, code, zoneDTO);
	}

	/**
	 * 文件导入缓存：获得仓库信息
	 *
	 * @param code 库区编码
	 * @return 库区信息
	 */
	public static ZoneDTO get(String code) {
		return CacheUtil.get(NODES_FLASH, ZONE_CACHE_CODE_IMPORT, code, () -> {
			return null;
		});
	}

	/**
	 * 文件导入缓存：从缓存中移除
	 *
	 * @param code 库区编码
	 */
	public static void remove(String code) {
		CacheUtil.evict(NODES_FLASH, ZONE_CACHE_CODE_IMPORT, code);
	}
}
