
package org.nodes.wms.core.common.service;

import org.nodes.wms.core.common.entity.Contacts;
import org.springblade.core.mp.base.BaseService;

/**
 * 服务类
 *
 * @author pengwei
 * @since 2019-12-06
 */
public interface IContactsService extends BaseService<Contacts> {

	/**
	 * 删除联系人
	 *
	 * @param dataId   数据ID
	 * @param dataType 数据类型
	 * @param isDeleted 删除标记
	 * @return 是否成功
	 */
	boolean remove(Long dataId, Integer dataType, Integer isDeleted);
}
