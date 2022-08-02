package org.nodes.wms.biz.task;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.task.dto.input.AgainIssuedlTask;
import org.nodes.wms.dao.task.dto.input.CancelTaskRequest;
import org.nodes.wms.dao.task.dto.input.StopTaskRequest;
import org.nodes.wms.dao.task.dto.input.TaskDetailPageRequest;
import org.nodes.wms.dao.task.dto.output.TaskDetailExcelResponse;
import org.nodes.wms.dao.task.dto.output.TaskDetailPageResponse;
import org.nodes.wms.dao.task.entities.TaskDetail;
import org.springblade.core.mp.support.Query;

import java.util.HashMap;
import java.util.List;

/**
 *
 */
public interface TaskBiz {
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
	List<TaskDetailExcelResponse> selectTaskDetailList(HashMap<String, Object> params);

	/**
	 * 停止任务的动作
	 *
	 * @param request 停止任务的参数
	 */
	void stop(StopTaskRequest request);

	/**
	 * 取消任务的动作
	 *
	 * @param request 取消任务的参数
	 */
	void cancel(CancelTaskRequest request);

	/**
	 * 重新下发任务的动作
	 *
	 * @param request 重新下发任务的参数
	 */
	void restart(AgainIssuedlTask request);

	/**
	 * 修改任务状态
	 *
	 * @param taskDetail 包含任务明细ID，任务状态
	 */
	Boolean updateTaskState(TaskDetail taskDetail);

}
