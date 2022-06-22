package org.nodes.wms.dao.basics.warehouse;

import org.nodes.wms.dao.basics.warehouse.entities.SysAuth;

/**
 * 仓库授权Dao接口
 **/
public interface SysAuthDao {

	/**
	 * 新增一条授权记录
	 *
	 * @param sysAuth: 授权对象
	 */
	void insertOne(SysAuth sysAuth);

	/**
	 * 获取一条仓库授权记录
	 *
	 * @return SysAuth 授权实体
	 */
	SysAuth getOne();
}
