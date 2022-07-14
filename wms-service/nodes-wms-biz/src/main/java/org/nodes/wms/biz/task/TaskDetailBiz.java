package org.nodes.wms.biz.task;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.task.dto.input.TaskDetailPageRequest;
import org.nodes.wms.dao.task.dto.output.TaskDetailExcelResponse;
import org.nodes.wms.dao.task.dto.output.TaskDetailPageResponse;
import org.springblade.core.mp.support.Query;

import java.util.HashMap;
import java.util.List;

/**
 *
 */
public interface TaskDetailBiz {
	/**
	 * 任务详情分页
	 *
	 * @param request 任务详情分页参数
	 * @param query   分页条件
	 * @return 按照条件查询出来的任务详情
	 */
	Page<TaskDetailPageResponse> selectPage(TaskDetailPageRequest request, Query query);

	/**
	 * 任务详情
	 *
	 * @param params 任务详情请求参数
	 * @return 按照条件查询出来的任务详情
	 */
	List<TaskDetailExcelResponse> selectTaskList(HashMap<String, Object> params);
}
