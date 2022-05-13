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
	public Owner findById(Long woId);
}
