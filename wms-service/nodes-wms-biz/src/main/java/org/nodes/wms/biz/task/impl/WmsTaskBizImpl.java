package org.nodes.wms.biz.task.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.task.WmsTaskBiz;
import org.nodes.wms.dao.task.TaskDetailDao;
import org.nodes.wms.dao.task.WmsTaskDao;
import org.nodes.wms.dao.task.dto.input.AgainIssuedlTask;
import org.nodes.wms.dao.task.dto.input.CancelTaskRequest;
import org.nodes.wms.dao.task.dto.input.StopTaskRequest;
import org.nodes.wms.dao.task.dto.input.TaskPageQuery;
import org.nodes.wms.dao.task.dto.output.TaskDetailExcelResponse;
import org.nodes.wms.dao.task.dto.output.TaskPageResponse;
import org.nodes.wms.dao.task.entities.TaskDetail;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
public class WmsTaskBizImpl implements WmsTaskBiz {
	private final TaskDetailDao taskDetailDao;
	private final WmsTaskDao wmsTaskDao;

	@Override
	public Page<TaskPageResponse> page(TaskPageQuery taskPageQuery, Query query) {
		return wmsTaskDao.getPage(Condition.getPage(query), taskPageQuery);
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

	@Override
	public void export(TaskPageQuery taskPageQuery, HttpServletResponse response) {
		IPage<Object> page = new Page<>();
		page.setCurrent(1);
		page.setSize(100000);
		List<TaskPageResponse> taskPageResponseList = wmsTaskDao.getPage(page, taskPageQuery).getRecords();
		ExcelUtil.export(response, "工作任务", "工作任务表", taskPageResponseList, TaskPageResponse.class);
	}
}
