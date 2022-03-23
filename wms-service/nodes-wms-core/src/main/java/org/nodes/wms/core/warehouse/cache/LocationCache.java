package org.nodes.wms.core.warehouse.cache;

import org.nodes.wms.core.warehouse.dto.LocationDTO;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static org.nodes.core.base.cache.CacheNames.NODES_FLASH;

/**
 * 库位缓存
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class LocationCache {

	public static final String LOCATION_CACHE = "wms:location:";

	static final String LOCATION_ID = "location:id:";

	static final String LOCATION_CODE = "location:code";

	/**
	 * 库位编码：文件导入使用
	 */
	private static final String LOCATION_CACHE_CODE_IMPORT = "location:code:import";

	private static ILocationService locationService;

	static {
		locationService = SpringUtil.getBean(ILocationService.class);
	}

	/**
	 * 获得库位信息
	 *
	 * @param id 库位ID
	 * @return 库位信息
	 */
	public static Location getById(Long id) {
		return CacheUtil.get(LOCATION_CACHE, LOCATION_ID, id, () -> locationService.getById(id));
	}

	public static Location getByCode(String code) {
		return CacheUtil.get(LOCATION_CACHE, LOCATION_CODE, code, () -> {
			return locationService.list(Condition.getQueryWrapper(new Location()).lambda()
				.eq(Location::getLocCode, code))
				.stream().findFirst().orElse(null);
		});
	}


	/**
	 * 获取并验证库位(业务处理时使用)
	 *
	 * @param id 库位id
	 * @return 库位信息
	 */
	public static Location getValid(Long id) {
		return getValid(id, null);
	}

	/**
	 * 获取并验证库位(业务处理时使用)
	 *
	 * @param id 库位id
	 * @return 库位信息
	 */
	public static Location getValid(Long id, String countBillNo) {
		Location location = getById(id);

		validData(id, null, location, countBillNo);

		return location;
	}

	/**
	 * 获取并验证库位(业务处理时使用)
	 *
	 * @param locCode 库位编码
	 * @return 库位信息
	 */
	public static Location getValid(String locCode) {
		return getValid(locCode, null);
	}

	/**
	 * 获取并验证库位(业务处理时使用)
	 *
	 * @param locCode     库位编码
	 * @param countBillNo 盘点单编码
	 * @return 库位信息
	 */
	public static Location getValid(String locCode, String countBillNo) {
		Location location = getByCode(locCode);

		validData(null, locCode, location, countBillNo);

		return location;
	}

	/**
	 * 验证库位(业务处理时使用)
	 *
	 * @param location 库位信息
	 */
	public static void validData(Location location) {
		validData(null, null, location, null);
	}

	/**
	 * 验证库位(业务处理时使用)
	 *
	 * @param location    库位信息
	 * @param countBillNo 盘点单编码
	 */
	public static void validData(Long locId, String locCode, Location location, String countBillNo) {
		if (Func.isEmpty(location)) {
			List<String> errMsgList = new ArrayList<>();
			if (Func.isNotEmpty(locId)) {
				errMsgList.add("库位ID：" + locId);
			}
			if (Func.isNotEmpty(locCode)) {
				errMsgList.add("库位编码：" + locCode);
			}
			String errMsg = Func.isEmpty(errMsgList) ? StringPool.EMPTY : StringUtil.join(errMsgList);
			throw new ServiceException(String.format("指定的库位不存在！%s", errMsg));
		}
		if (!StringUtil.isEmpty(location.getLockFlag())) {
			throw new ServiceException(
				String.format("指定的库存ID对应的库位为锁定状态（库位编码：%s）！", location.getLocCode()));
		}
	}

	/**
	 * 文件导入缓存：存储缓存
	 *
	 * @param locationDTO 库位信息
	 */
	public static void putDTO(String code, LocationDTO locationDTO) {
		CacheUtil.put(LOCATION_CACHE, LOCATION_CACHE_CODE_IMPORT, code, locationDTO);
	}

	/**
	 * 文件导入缓存：获得库位信息
	 *
	 * @param code 库位编码
	 * @return 库位信息
	 */
	public static LocationDTO getDTO(String code) {
		return CacheUtil.get(LOCATION_CACHE, LOCATION_CACHE_CODE_IMPORT, code, () -> {
			return null;
		});
	}

	/**
	 * 文件导入缓存：从缓存中移除
	 *
	 * @param code 库位编码
	 */
	public static void removeDTO(String code) {
		CacheUtil.evict(LOCATION_CACHE, LOCATION_CACHE_CODE_IMPORT, code);
	}

	/**
	 * 文件导入缓存：存储到临时缓存
	 *
	 * @param code     库位编码
	 * @param location 库位信息
	 */
	public static void put(String code, LocationDTO location) {
		CacheUtil.put(NODES_FLASH, LOCATION_CACHE_CODE_IMPORT, code, location);
	}

	/**
	 * 文件导入缓存：获得库位信息
	 *
	 * @param code 库位编码
	 * @return 库位信息
	 */
	public static LocationDTO get(String code) {
		return CacheUtil.get(NODES_FLASH, LOCATION_CACHE_CODE_IMPORT, code, () -> {
			return null;
		});
	}

	/**
	 * 文件导入缓存：从缓存中移除
	 *
	 * @param code 库位编码
	 */
	public static void remove(String code) {
		CacheUtil.evict(NODES_FLASH, LOCATION_CACHE_CODE_IMPORT, code);
	}
}
