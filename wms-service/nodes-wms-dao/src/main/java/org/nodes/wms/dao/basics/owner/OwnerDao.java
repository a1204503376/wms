package org.nodes.wms.dao.basics.owner;

import org.nodes.wms.dao.basics.owner.entities.Owner;

import java.util.List;

/**
 * 货主 DAO接口
 */

public interface OwnerDao {

	/**
	 * 查询货主下拉数据
	 *
	 * @return List<OwnerSelectResponse>
	 */
	List<Owner> selectOwnerSelectResponseList();

	/**
	 * 根据id查找货主信息
	 *
	 * @param woId: 货主id
	 * @return Owner
	 */
    Owner getById(Long woId);
}
