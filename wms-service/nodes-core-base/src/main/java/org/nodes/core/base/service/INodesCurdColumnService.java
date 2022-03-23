package org.nodes.core.base.service;

import org.nodes.core.base.dto.NodesCurdColumnDTO;
import org.nodes.core.base.entity.NodesCurdColumn;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.secure.BladeUser;

import java.util.List;

/**
 * 列显隐表 服务类
 *
 * @author wangYuNan
 * @since 2021-07-06
 */
public interface INodesCurdColumnService extends BaseService<NodesCurdColumn> {
	/**
	 * 获取数据权限
	 *
	 * @param nodesCurdColumn
	 * @return
	 */
	List<NodesCurdColumn> list( BladeUser bladeUser);
	/**
	 * 数据权限提交
	 *
	 * @param nodesCurdColumn
	 * @return
	 */
	boolean saveOrUpdate(NodesCurdColumnDTO nodesCurdColumnDTO, BladeUser bladeUser);
}
