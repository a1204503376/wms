package org.nodes.wms.biz.task;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.task.dto.input.AgainIssuedlTask;
import org.nodes.wms.dao.task.dto.input.CancelTaskRequest;
import org.nodes.wms.dao.task.dto.input.StopTaskRequest;
import org.nodes.wms.dao.task.dto.input.TaskPageQuery;
import org.nodes.wms.dao.task.dto.output.TaskDetailExcelResponse;
import org.nodes.wms.dao.task.dto.output.TaskPageResponse;
import org.nodes.wms.dao.task.entities.TaskDetail;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * wms工作任务BIZ
 *
 * @author nodesc
 */
public interface WmsTaskBiz {
	/**
	 * 工作任务分页
	 *
	 * @param taskPageQuery 查询参数
	 * @param query         分页参数
	 * @return 分页对象
	 */
	Page<TaskPageResponse> page(TaskPageQuery taskPageQuery, Query query);

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

	/**
	 * 导出工作任务列表
	 *
	 * @param taskPageQuery 查询参数
	 * @param response      响应对象
	 */
	void export(TaskPageQuery taskPageQuery, HttpServletResponse response);
}
