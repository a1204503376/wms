package org.nodes.wms.dao.task;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.task.dto.input.TaskDetailPageRequest;
import org.nodes.wms.dao.task.dto.output.TaskDetailExcelResponse;
import org.nodes.wms.dao.task.dto.output.TaskDetailPageResponse;
import org.nodes.wms.dao.task.entities.TaskDetail;
import org.springblade.core.mp.base.BaseService;

import java.util.HashMap;
import java.util.List;

public interface TaskDetailDao extends BaseService<TaskDetail> {

	/**
	 * 分页查询
	 *
	 * @param page    底部分页参数
	 * @param request 查询条件
	 * @return 分页数据
	 */
	Page<TaskDetailPageResponse> getPage(IPage<?> page, TaskDetailPageRequest request);

	/**
	 * 根据条件查询对应的数据
	 *
	 * @param params 查询条件
	 * @return TaskDetailExcelResponse集合
	 */
	List<TaskDetailExcelResponse> getTaskList(HashMap<String, Object> params);

	/**
	 * 根据任务明细ID更新任务状态
	 *
	 * @param detail 包含任务状态以及明细ID和头表ID
	 * @return 是否成功
	 */
	Boolean update(TaskDetail detail);
}
