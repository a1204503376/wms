package org.nodes.wms.core.warehouse.cache;


import org.nodes.wms.core.warehouse.dto.WarehouseDTO;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.io.Serializable;
import java.util.*;

import static org.nodes.core.base.cache.CacheNames.NODES_FLASH;

/**
 * 库房缓存
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class WarehouseCache {

	public static final String WAREHOUSE_CACHE = "wms:warehouse";

	static final String WAREHOUSE_ID = "warehouse:id:";

	/**
	 * 库房编码：文件导入使用
	 */
	static final String WAREHOUSE_CACHE_CODE_IMPORT = "warehouse:code:import";

	static IWarehouseService warehouseService;

	static {
		warehouseService = SpringUtil.getBean(IWarehouseService.class);
	}

	/**
	 * 获取库房的数量
	 *
	 * @return 库房数量
	 */
	public static Integer size() {
		return warehouseService.count();
	}

	public static Warehouse getValid(Long whId) {
		Warehouse warehouse = getById(whId);
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("库房(id=" + whId + ") 不存在或已删除！");
		}
		return warehouse;
	}

	public static Warehouse getById(Long whId) {
		return CacheUtil.get(WAREHOUSE_CACHE, WAREHOUSE_ID, whId, () -> warehouseService.getById(whId));
	}
	/**
	 * 文件导入缓存：获得仓库信息
	 *
	 * @param code 仓库编码
	 * @return 仓库信息
	 */
	public static WarehouseDTO getDTO(String code) {
		return CacheUtil.get(WAREHOUSE_CACHE, WAREHOUSE_CACHE_CODE_IMPORT, code, () -> {
			return null;
		});
	}

	/**
	 * 文件导入缓存：从缓存中移除
	 *
	 * @param code 仓库编码
	 */
	public static void removeDTO(String code) {
		CacheUtil.evict(WAREHOUSE_CACHE, WAREHOUSE_CACHE_CODE_IMPORT, code);
	}

	/**
	 * 文件导入缓存：存储到临时缓存
	 *
	 * @param code  仓库编码
	 * @param warehouseDTO 仓库信息
	 */
	public static void put(String code, WarehouseDTO warehouseDTO) {
		CacheUtil.put(NODES_FLASH, WAREHOUSE_CACHE_CODE_IMPORT, code, warehouseDTO);
	}

	/**
	 * 文件导入缓存：获得仓库信息
	 *
	 * @param code 仓库编码
	 * @return 仓库信息
	 */
	public static WarehouseDTO get(String code) {
		return CacheUtil.get(NODES_FLASH, WAREHOUSE_CACHE_CODE_IMPORT, code, () -> {
			return null;
		});
	}
	/**
	 * 文件导入缓存：从缓存中移除
	 *
	 * @param code 仓库编码
	 */
	public static void remove(String code) {
		CacheUtil.evict(NODES_FLASH, WAREHOUSE_CACHE_CODE_IMPORT, code);
	}
}
