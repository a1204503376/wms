package org.nodes.wms.biz.task.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.task.WmsTaskBiz;
import org.nodes.wms.dao.task.TaskDetailDao;
import org.nodes.wms.dao.task.dto.input.AgainIssuedlTask;
import org.nodes.wms.dao.task.dto.input.CancelTaskRequest;
import org.nodes.wms.dao.task.dto.input.StopTaskRequest;
import org.nodes.wms.dao.task.dto.input.TaskDetailPageRequest;
import org.nodes.wms.dao.task.dto.output.TaskDetailExcelResponse;
import org.nodes.wms.dao.task.dto.output.TaskDetailPageResponse;
import org.nodes.wms.dao.task.entities.TaskDetail;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
public class WmsTaskBizImpl implements WmsTaskBiz {
	private final TaskDetailDao taskDetailDao;

	@Override
	public Page<TaskDetailPageResponse> selectPage(TaskDetailPageRequest request, Query query) {
		return taskDetailDao.getPage(Condition.getPage(query), request);
	}

	@Override
	public List<TaskDetailExcelResponse> selectTaskDetailList(HashMap<String, Object> params) {
		return taskDetailDao.getTaskList(params);
	}

	@Override
	public void stop(StopTaskRequest request) {

	}

	@Override
	public void cancel(CancelTaskRequest request) {

	}

	@Override
	public void restart(AgainIssuedlTask request) {

	}

	@Override
	public Boolean updateTaskState(TaskDetail taskDetail) {
		return taskDetailDao.update(taskDetail);
	}
}
