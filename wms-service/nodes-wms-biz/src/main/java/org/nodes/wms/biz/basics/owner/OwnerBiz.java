package org.nodes.wms.biz.basics.owner;

import org.nodes.wms.dao.basics.owner.dto.OwnerSelectResponse;
import org.nodes.wms.dao.basics.owner.entities.Owner;

import java.util.List;

/**
 * 货主 业务接口
 **/
public interface OwnerBiz {

	/**
	 * 获取货主下拉数据
	 *
	 * @return 货主信息集合
	 */
	List<OwnerSelectResponse> getOwnerSelectResponseList();

	/**
	 * 根据id查找货主信息
	 *
	 * @param woId: 货主id
	 * @return Owner
	 */
	Owner findById(Long woId);

	/**
	 * 根据编码查找货主信息
	 *
	 * @param ownerCode: 货主编码
	 * @return Owner
	 */
	Owner findByCode(String ownerCode);

	/**
	 * 获取第一条货主信息
	 *
	 * @return
	 */
	Owner getFirst();


	/**
	 * 根据名称查找货主信息
	 *
	 * @param ownerName: 货主名称
	 * @return Owner
	 */
	Owner findByName(String ownerName);
}
