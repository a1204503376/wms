package org.nodes.wms.core.basedata.cache;

import org.nodes.wms.core.basedata.dto.OwnerDTO;
import org.nodes.wms.core.basedata.entity.Owner;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.nodes.core.base.cache.CacheNames.NODES_FLASH;


/**
 * 货主缓存
 *
 * @Author zx
 * @Date 2019/12/23
 **/
public class OwnerCache {
	/**
	 * 货主编码：文件导入使用
	 */
	static final String OWNER_CACHE_CODE_IMPORT = "owner:code:import";

	static Map<Long, Owner> ownerMap = new HashMap<>();

	static IOwnerService ownerService;

	static {
		ownerService = SpringUtil.getBean(IOwnerService.class);

		ownerService.list().forEach(owner -> {
			ownerMap.put(owner.getWoId(), owner);
		});
	}

	/**
	 * 获得货主信息
	 *
	 * @param id
	 * @return
	 */
/*	public static Owner getById(Long id) {
		return ownerMap.getOrDefault(id, null);
	}

	*//**
	 * 获得货主信息
	 *
	 * @param code 货主编码
	 * @return 货主信息
	 *//*
	public static Owner getByCode(String code) {
		return ownerMap.values().stream().filter(owner -> {
			return owner.getOwnerCode().equals(code);
		}).findFirst().orElse(null);
	}

	*//**
	 * 获取货主列表
	 *
	 * @return 货主列表
	 *//*
	public static List<Owner> list() {
		return new ArrayList<>(ownerMap.values());
	}

	*//**
	 * 保存 或 修改 货主信息
	 *
	 * @param owner 货主信息
	 *//*
	public static void saveOrUpdate(Owner owner) {
		Owner findObj = getById(owner.getWoId());
		if (Func.isEmpty(findObj)) {
			ownerMap.put(owner.getWoId(), owner);
		} else {
			ownerMap.replace(owner.getWoId(), owner);
		}
	}

	*//**
	 * 删除 货主信息
	 *
	 * @param id 主键ID
	 *//*
	public static void removeById(Long id) {
		ownerMap.remove(id);
	}*/

	/**
	 * 文件导入缓存：存储到临时缓存
	 *
	 * @param code  货主编码
	 * @param owner 货主信息
	 */
	public static void put(String code, OwnerDTO owner) {
		CacheUtil.put(NODES_FLASH, OWNER_CACHE_CODE_IMPORT, code, owner);
	}

	/**
	 * 文件导入缓存：获得货主信息
	 *
	 * @param code 货主编码
	 * @return 货主信息
	 */
	public static OwnerDTO get(String code) {
		return CacheUtil.get(NODES_FLASH, OWNER_CACHE_CODE_IMPORT, code, () -> {
			return null;
		});
	}

	/**
	 * 文件导入缓存：从缓存中移除
	 *
	 * @param code 货主编码
	 */
	public static void remove(String code) {
		CacheUtil.evict(NODES_FLASH, OWNER_CACHE_CODE_IMPORT, code);
	}

}
