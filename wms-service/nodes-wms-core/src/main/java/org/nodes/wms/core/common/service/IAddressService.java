
package org.nodes.wms.core.common.service;

import org.nodes.wms.core.common.entity.Address;
import org.springblade.core.mp.base.BaseService;

/**
 * 服务类
 *
 * @author pengwei
 * @since 2019-12-06
 */
public interface IAddressService extends BaseService<Address> {

	/**
	 * 根据 data_id 和 data_type 删除地址
	 *
	 * @param dataId   数据ID
	 * @param dataType 数据类型
	 * @param isDeleted 删除标记
	 * @return 是否成功
	 */
	boolean remove(Long dataId, Integer dataType, Integer isDeleted);
}
