package org.nodes.wms.controller.common;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.common.query.NodesQueryPlanBiz;
import org.nodes.wms.dao.common.query.dto.input.AddNodesQueryPlanRequest;
import org.nodes.wms.dao.common.query.dto.input.DeleteQueryPlanRequest;
import org.nodes.wms.dao.common.query.dto.input.FindAllQueryPlan;
import org.nodes.wms.dao.common.query.dto.input.UpdateQueryPlanRequest;
import org.nodes.wms.dao.common.query.dto.output.FindAllNodesQueryPlan;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 查询方案API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "queryPlan")
public class NodesQueryPlanController {
	private final NodesQueryPlanBiz queryPlanBiz;

	/**
	 * 获取当前所在页面的查询方案
	 *
	 * @param queryPlan 包含页面路径URL
	 * @return 查询方案List
	 */
	@PostMapping("/findAllNodesQueryPlan")
	public R<List<FindAllNodesQueryPlan>> findAllNodesQueryPlan(@RequestBody FindAllQueryPlan queryPlan) {
		return R.data(queryPlanBiz.findAll(queryPlan));
	}

	/**
	 * 新增查询方案
	 *
	 * @param request 新增查询方案请求对象
	 */
	@PostMapping("/insert")
	public void insert(@RequestBody AddNodesQueryPlanRequest request) {
		queryPlanBiz.insert(request);
	}

	/**
	 * 删除查询方案
	 *
	 * @param request 删除查询方案请求对象
	 */
	@PostMapping("/delete")
	public void delete(@RequestBody DeleteQueryPlanRequest request) {
		queryPlanBiz.delete(request);
	}

	/**
	 * 设置当前选中的为默认查询方案
	 *
	 * @param request 修改默认查询方案请求对象
	 */
	@PostMapping("/update")
	public void update(@RequestBody UpdateQueryPlanRequest request) {
		queryPlanBiz.update(request);
	}

	/**
	 * 取消默认查询方案
	 *
	 * @param request 修改默认查询方案请求对象
	 */
	@PostMapping("/cancel")
	public void cancel(@RequestBody UpdateQueryPlanRequest request) {
		queryPlanBiz.cancel(request);
	}
}
