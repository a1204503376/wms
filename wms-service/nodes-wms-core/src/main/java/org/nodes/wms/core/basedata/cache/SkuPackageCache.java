package org.nodes.wms.core.basedata.cache;

import org.nodes.wms.core.basedata.dto.SkuPackageDTO;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.basedata.service.ISkuPackageService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.io.Serializable;
import java.util.*;

import static org.nodes.core.base.cache.CacheNames.NODES_FLASH;


/**
 * 包装缓存
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class SkuPackageCache {

	public static final String SKU_PACKAGE_CACHE = "wms:sku_package";

	static final String SKU_PACKAGE_ID = "sku_package:id:";

	static ISkuPackageService skuPackageService;

	/**
	 * 包装名称：文件导入使用
	 */
	private static final String SKUPACKAGE_CACHE_NAME_IMPORT = "skupackage:wspName:import";

	static {
		skuPackageService = SpringUtil.getBean(ISkuPackageService.class);
	}

	public static SkuPackage getById(Long wspId) {
		return CacheUtil.get(SKU_PACKAGE_CACHE, SKU_PACKAGE_ID, wspId, ()->skuPackageService.getById(wspId));
	}

	/**
	 * 文件导入缓存：获得货主信息
	 *
	 * @param code 货主编码
	 * @return 货主信息
	 */
	public static SkuPackageDTO get(String code) {
		return CacheUtil.get(NODES_FLASH, SKUPACKAGE_CACHE_NAME_IMPORT, code, () -> {
			return null;
		});
	}


	public static void remove(String code) {
		CacheUtil.evict(NODES_FLASH, SKUPACKAGE_CACHE_NAME_IMPORT, code);
	}

	public static void put(String code, SkuPackageDTO skuPackageDTO) {
		CacheUtil.put(NODES_FLASH, SKUPACKAGE_CACHE_NAME_IMPORT, code, skuPackageDTO);
	}
}
