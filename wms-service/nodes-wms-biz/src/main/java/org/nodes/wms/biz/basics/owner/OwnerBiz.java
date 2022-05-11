package org.nodes.wms.biz.basics.owner;

import org.nodes.wms.dao.basics.owner.dto.OwnerSelectResponse;

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
}
