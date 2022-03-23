
package org.nodes.wms.core.common.service.impl;

import org.nodes.wms.core.common.cache.AddressCache;
import org.nodes.wms.core.common.entity.Address;
import org.nodes.wms.core.common.mapper.AddressMapper;
import org.nodes.wms.core.common.service.IAddressService;
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
public class AddressServiceImpl<M extends AddressMapper, T extends Address>
	extends BaseServiceImpl<AddressMapper, Address>
	implements IAddressService {

	@Override
	public boolean remove(Long dataId, Integer dataType, Integer isDeleted) {
		boolean result = super.remove(Condition.getQueryWrapper(new Address()).lambda()
			.eq(Address::getDataId, dataId)
			.eq(Address::getAddressDataType, dataType)
			.eq(Address::getIsDeleted, isDeleted));
		if (result) {
			//AddressCache.remove(dataId, dataType);
		}
		return result;
	}

	@Override
	public boolean saveOrUpdate(Address address) {
		boolean result = super.saveOrUpdate(address);
		if (result) {
			//AddressCache.saveOrUpdate(address);
		}
		return result;
	}

	@Override
	public boolean removeById(Serializable id) {
		boolean result = super.removeById(id);
		if (result) {
			//AddressCache.removeById((Long) id);
		}
		return result;
	}
}
