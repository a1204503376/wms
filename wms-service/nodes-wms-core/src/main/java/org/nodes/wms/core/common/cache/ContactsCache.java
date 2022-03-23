package org.nodes.wms.core.common.cache;

import org.nodes.wms.core.common.entity.Contacts;
import org.nodes.wms.core.common.service.IContactsService;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 联系人缓存
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class ContactsCache {

	static Map<Long, Contacts> contactsMap = new HashMap<>();

	private static IContactsService contactsService;

	static {
		contactsService = SpringUtil.getBean(IContactsService.class);

		contactsService.list().forEach(contacts -> {
			contactsMap.put(contacts.getPcId(), contacts);
		});
	}

	/**
	 * 获得联系人信息
	 *
	 * @param id
	 * @return
	 */
	/*public static Contacts getById(Long id) {
		return contactsMap.getOrDefault(id, null);
	}

	*//**
	 * 获取联系人集合
	 *
	 * @return 联系人集合
	 *//*
	public static List<Contacts> list() {
		return new ArrayList<>(contactsMap.values());
	}

	*//**
	 * 获取联系人集合
	 *
	 * @param dataId   数据ID
	 * @param dataType 数据类型
	 * @return 联系人集合
	 *//*
	public static List<Contacts> list(Long dataId, Integer dataType) {
		return contactsMap.values().stream().filter(contacts -> {
			return contacts.getDataId().equals(dataId) && contacts.getContactsDataType().equals(dataType);
		}).collect(Collectors.toList());
	}

	*//**
	 * 保存 或者 修改 联系人
	 *
	 * @param contacts 联系人信息
	 *//*
	public static void saveOrUpdate(Contacts contacts) {
		Contacts findObj = getById(contacts.getPcId());
		if (Func.isEmpty(findObj)) {
			contactsMap.put(contacts.getPcId(), contacts);
		} else {
			contactsMap.replace(contacts.getPcId(), contacts);
		}
	}

	*//**
	 * 删除联系人信息
	 *
	 * @param pcId 主键ID
	 *//*
	public static void removeById(Long pcId) {
		contactsMap.remove(pcId);
	}

	*//**
	 * 删除联系人信息
	 *
	 * @param dataId   数据ID
	 * @param dataType 数据类型
	 *//*
	public static void remove(Long dataId, Integer dataType) {
		List<Contacts> contactsList = list(dataId, dataType);
		if (Func.isNotEmpty(contactsList)) {
			for (Contacts contacts : contactsList) {
				removeById(contacts.getPcId());
			}
		}
	}*/
}
