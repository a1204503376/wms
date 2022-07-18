package org.nodes.wms.dao.task.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.task.TaskDetailDao;
import org.nodes.wms.dao.task.dto.input.TaskDetailPageRequest;
import org.nodes.wms.dao.task.dto.output.TaskDetailExcelResponse;
import org.nodes.wms.dao.task.dto.output.TaskDetailPageResponse;
import org.nodes.wms.dao.task.entities.TaskDetail;
import org.nodes.wms.dao.task.mapper.TaskDetailMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * 任务详情DAO
 */
@Repository
@RequiredArgsConstructor
public class TaskDetailDaoImpl extends BaseServiceImpl<TaskDetailMapper, TaskDetail>
	implements TaskDetailDao {
	private final TaskDetailMapper taskDetailMapper;

	@Override
	public Page<TaskDetailPageResponse> getPage(IPage<?> page, TaskDetailPageRequest request) {
		return taskDetailMapper.getPage(page, request);
	}

	@Override
	public List<TaskDetailExcelResponse> getTaskList(HashMap<String, Object> params) {
		return taskDetailMapper.getTaskList(params);
	}

	@Override
	public Boolean update(TaskDetail detail) {
		AssertUtil.notNull(detail.getId(), "任务明细ID为空");
		AssertUtil.notNull(detail.getTaskDetailStatus(), "任务明细状态为空");
		UpdateWrapper updateWrapper = new UpdateWrapper();
		updateWrapper.eq("id", detail.getId());
		updateWrapper.set("task_detail_status", detail.getTaskDetailStatus());
		return super.update(updateWrapper);
	}
}
