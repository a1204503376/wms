package org.nodes.wms.biz.common.query;

import org.nodes.wms.dao.common.query.dto.input.AddNodesQueryPlanRequest;
import org.nodes.wms.dao.common.query.dto.input.DeleteQueryPlanRequest;
import org.nodes.wms.dao.common.query.dto.input.FindAllQueryPlan;
import org.nodes.wms.dao.common.query.dto.input.UpdateQueryPlanRequest;
import org.nodes.wms.dao.common.query.dto.output.FindAllNodesQueryPlan;

import java.util.List;

/**
 * @author nodes
 * 查询方案BIZ
 */
public interface NodesQueryPlanBiz {
	/**
	 * 根据用户ID和页面路径获取查询方案
	 *
	 * @param queryPlan 查询 查询方案请求对象
	 * @return 查询方案集合
	 */
	List<FindAllNodesQueryPlan> findAll(FindAllQueryPlan queryPlan);

	/**
	 * 新增查询方案
	 *
	 * @param request 新增查询方案请求对象
	 * @return 是否成功
	 */
	void insert(AddNodesQueryPlanRequest request);

	/**
	 * 删除查询方案
	 *
	 * @param request 删除查询方案请求对象
	 * @return 是否成功
	 */
	void delete(DeleteQueryPlanRequest request);

	/**
	 * 修改查询方案
	 *
	 * @param request 修改查询方案请求对象
	 * @return 是否成功
	 */
	void update(UpdateQueryPlanRequest request);

	/**
	 * 取消默认查询方案
	 *
	 * @param request 修改查询方案请求对象
	 * @return 是否成功
	 */
	void cancel(UpdateQueryPlanRequest request);
}
