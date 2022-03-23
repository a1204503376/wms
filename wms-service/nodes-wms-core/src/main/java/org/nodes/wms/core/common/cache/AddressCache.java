package org.nodes.wms.core.common.cache;

import org.nodes.wms.core.common.entity.Address;
import org.nodes.wms.core.common.service.IAddressService;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 地址缓存
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class AddressCache {

	static Map<Long, Address> addressMap = new HashMap<>();

	static IAddressService addressService;

	static {
		addressService = SpringUtil.getBean(IAddressService.class);

		addressService.list().forEach(address -> {
			addressMap.put(address.getPaId(), address);
		});
	}

	/**
	 * 获得地址信息
	 *
	 * @param id
	 * @return
	 */
	/*public static Address getById(Long id) {
		return addressMap.getOrDefault(id, null);
	}

	*//**
	 * 获取地址集合
	 *
	 * @return 地址集合
	 *//*
	public static List<Address> list() {
		return new ArrayList<>(addressMap.values());
	}

	*//**
	 * 获取地址集合
	 *
	 * @param dataId   数据ID
	 * @param dataType 数据类型
	 * @return 地址集合
	 *//*
	public static List<Address> list(Long dataId, Integer dataType) {
		return addressMap.values().stream().filter(address -> {
			return address.getDataId().equals(dataId) && address.getAddressDataType().equals(dataType);
		}).collect(Collectors.toList());
	}

	*//**
	 * 保存 或 修改 地址信息
	 *
	 * @param address 地址信息
	 *//*
	public static void saveOrUpdate(Address address) {
		Address findObj = getById(address.getPaId());
		if (Func.isEmpty(findObj)) {
			addressMap.put(address.getPaId(), address);
		} else {
			addressMap.replace(address.getPaId(), address);
		}
	}

	*//**
	 * 删除地址信息
	 *
	 * @param paId 主键ID
	 *//*
	public static void removeById(Long paId) {
		addressMap.remove(paId);
	}

	*//**
	 * 删除地址信息
	 *
	 * @param dataId   数据ID
	 * @param dataType 数据类型
	 *//*
	public static void remove(Long dataId, Integer dataType) {
		List<Address> addressList = list(dataId, dataType);
		if (Func.isNotEmpty(addressList)) {
			for (Address address : addressList) {
				removeById(address.getPaId());
			}
		}
	}*/
}
