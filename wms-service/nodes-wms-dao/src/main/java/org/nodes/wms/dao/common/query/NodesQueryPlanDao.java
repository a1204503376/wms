package org.nodes.wms.dao.common.query;

import org.nodes.wms.dao.common.query.entities.NodesQueryPlan;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * @author nodes
 * 查询任务dao
 */
public interface NodesQueryPlanDao extends BaseService<NodesQueryPlan> {
	/**
	 * 根据用户ID和页面路径获取查询方案
	 *
	 * @param createUser 用户ID
	 * @param pageUrl    查询方案
	 * @return 查询方案集合
	 */
	List<NodesQueryPlan> getAll(Long createUser, String pageUrl);

	/**
	 * 新增查询方案
	 *
	 * @param nodesQueryPlan 查询方案
	 * @return 是否成功
	 */
	void insert(NodesQueryPlan nodesQueryPlan);

	/**
	 * 删除查询方案
	 *
	 * @param id 查询方案主键
	 * @return 是否成功
	 */
	void delete(Long id);

	/**
	 * 修改查询方案
	 *
	 * @param id        查询方案主键
	 * @param isDefault 是否默认
	 * @return 是否成功
	 */
	void update(Long id, Integer isDefault);

	/**
	 * 取消当前页面全部默认查询方案
	 *
	 * @param pageUrl   页面路径
	 * @param isDefault 是否默认
	 * @return 是否成功
	 */
	void update(String pageUrl, Integer isDefault);
}
