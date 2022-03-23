package org.nodes.wms.core.basedata.cache;

import org.nodes.wms.core.basedata.dto.EnterpriseDTO;
import org.nodes.wms.core.basedata.entity.Enterprise;
import org.nodes.wms.core.basedata.service.IEnterpriseService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.nodes.core.base.cache.CacheNames.NODES_FLASH;

/**
 * 来往企业缓存
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class EnterpriseCache {
	/**
	 * 来往企业编码：文件导入使用
	 */
	static final String ENTERPRISE_CACHE_CODE_IMPORT = "pubenterprise:code:import";
	static IEnterpriseService enterpriseService;

	static {
		enterpriseService = SpringUtil.getBean(IEnterpriseService.class);
	}
	/**
	 * 文件导入缓存：存储到临时缓存
	 *  @param code  企业编码
	 * @param enterprise 企业信息
	 */
	public static void put(String code, EnterpriseDTO enterprise) {
		CacheUtil.put(NODES_FLASH, ENTERPRISE_CACHE_CODE_IMPORT, code, enterprise);
	}


	/**
	 * 文件导入缓存：获得企业信息
	 *
	 * @param code 企业编码
	 * @return 企业信息
	 */
	public static EnterpriseDTO get(String code) {
		return CacheUtil.get(NODES_FLASH, ENTERPRISE_CACHE_CODE_IMPORT, code, () -> {
			return null;
		});
	}

	/**
	 * 文件导入缓存：从缓存中移除
	 *
	 * @param code 企业编码
	 */
	public static void remove(String code) {
		CacheUtil.evict(NODES_FLASH, ENTERPRISE_CACHE_CODE_IMPORT, code);
	}
}
