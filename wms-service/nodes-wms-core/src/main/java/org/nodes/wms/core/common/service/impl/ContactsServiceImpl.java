
package org.nodes.wms.core.common.service.impl;

import org.nodes.wms.core.common.cache.ContactsCache;
import org.nodes.wms.core.common.entity.Contacts;
import org.nodes.wms.core.common.mapper.ContactsMapper;
import org.nodes.wms.core.common.service.IContactsService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * 服务实现类
 *
 * @author pengwei
 * @since 2019-12-06
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class ContactsServiceImpl<M extends ContactsMapper, T extends Contacts>
	extends BaseServiceImpl<ContactsMapper, Contacts>
	implements IContactsService {

	@Override
	public boolean remove(Long dataId, Integer dataType, Integer isDeleted) {
		boolean result = super.remove(Condition.getQueryWrapper(new Contacts()).lambda()
			.eq(Contacts::getDataId, dataId)
			.eq(Contacts::getContactsDataType, dataType)
			.eq(Contacts::getIsDeleted, isDeleted));
		//ContactsCache.remove(dataId, dataType);
		return result;
	}

	@Override
	public boolean removeById(Serializable id) {
		boolean result = super.removeById(id);
		if (result) {
			//ContactsCache.removeById((Long) id);
		}
		return result;
	}

	@Override
	public boolean saveOrUpdate(Contacts entity) {
		boolean result = super.saveOrUpdate(entity);
		if (result) {
			//ContactsCache.saveOrUpdate(entity);
		}
		return result;
	}
}
