package org.nodes.wms.core.warehouse.cache;

import org.nodes.wms.core.warehouse.dto.LpnDTO;
import org.nodes.wms.core.warehouse.entity.Lpn;
import org.nodes.wms.core.warehouse.service.ILpnService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.HashMap;
import java.util.Map;

import static org.nodes.core.base.cache.CacheNames.NODES_FLASH;


/**
 * 容器缓存
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class LpnCache {

	/**
	 * 容器编码：文件导入使用
	 */
	static final String LPN_CACHE_CODE_IMPORT = "publpn:code:import";
	public static final String LPN_CACHE = "wms.lpn";
	//容器id
	private static final String LPN_CACHE_ID = "lpn:id";

	static Map<Long, Lpn> lpnMap = new HashMap<>();

	private static ILpnService lpnService;

	static {
		lpnService = SpringUtil.getBean(ILpnService.class);
	}

	/**
	 * 获得容器信息
	 * @param id
	 * @return
	 */
	/*public static Lpn getById(Long id) {
		return CacheUtil.get(LPN_CACHE, LPN_CACHE_ID, id, () -> lpnService.getById(id));
	}

	*//**
	 * 删除缓存
	 * @param id
	 *//*
	public static void removeById(Long id){
		CacheUtil.evict(LPN_CACHE, LPN_CACHE_ID, id);
	}

	*//**
	 * 批量删除缓存
	 * @param ids
	 *//*
	public void removeByIds(String ids){
		for (String id : ids.split(",")) {
			CacheUtil.evict(LPN_CACHE, LPN_CACHE_ID, id);
		}
	}
	*//**
	 * 获得容器信息
	 *
	 * @param code 容器编码
	 * @return 容器信息
	 *//*
	public static Lpn getByCode(String code) {
		return lpnMap.values().stream().filter(lpn -> {
			return lpn.getLpnCode().equals(code);
		}).findFirst().orElse(null);
	}*/

	/**
	 * 文件导入缓存：存储到临时缓存
	 *  @param code  容器编码
	 * @param lpn 容器信息
	 */
	public static void put(String code, LpnDTO lpn) {
		CacheUtil.put(NODES_FLASH, LPN_CACHE_CODE_IMPORT, code, lpn);
	}

	/**
	 * 文件导入缓存：获得容器信息
	 *
	 * @param code 容器编码
	 * @return 容器信息
	 */
	public static LpnDTO get(String code) {
		return CacheUtil.get(NODES_FLASH, LPN_CACHE_CODE_IMPORT, code, () -> {
			return null;
		});
	}

	/**
	 * 文件导入缓存：从缓存中移除
	 *
	 * @param code 容器编码
	 */
	public static void remove(String code) {
		CacheUtil.evict(NODES_FLASH, LPN_CACHE_CODE_IMPORT, code);
	}
	/**
	 * 保存 或 更新 容器
	 *
	 * @param lpn 容器
	 */
	/*public static void saveOrUpdate(Lpn lpn) {
		Lpn findObj = getById(lpn.getLpnId());
		if (Func.isEmpty(findObj)) {
			lpnMap.put(lpn.getLpnId(), lpn);
		} else {
			lpnMap.replace(lpn.getLpnId(), lpn);
		}
	}*/
}
