package org.nodes.wms.biz.task.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.task.AgvTask;
import org.nodes.wms.biz.task.WmsTaskBiz;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.task.TaskDetailDao;
import org.nodes.wms.dao.task.WmsTaskDao;
import org.nodes.wms.dao.task.dto.input.StopTaskRequest;
import org.nodes.wms.dao.task.dto.input.TaskPageQuery;
import org.nodes.wms.dao.task.dto.output.TaskDetailExcelResponse;
import org.nodes.wms.dao.task.dto.output.TaskPageResponse;
import org.nodes.wms.dao.task.entities.TaskDetail;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
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
	private final AgvTask agvTask;
	private final LogBiz logBiz;


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
	public void cancel(List<Long> taskIdList) {
		List<WmsTask> wmsTaskList = wmsTaskDao.getTaskByIds(taskIdList);
		// 校验任务状态是否为异常中断，否则抛异常
		wmsTaskList.forEach(task -> {
			if (!WmsTaskStateEnum.ABNORMAL.getCode().equals(task.getTaskState().getCode())) {
				throw new ServiceException("继续执行任务失败，任务状态只能是：异常中断中");
			}
		});
		// 取消任务
		agvTask.cancel();
		// 更新任务状态、记录日志
		wmsTaskList.forEach(task -> {
			wmsTaskDao.updateState(task.getTaskId(), WmsTaskStateEnum.CANCELED);
			logBiz.auditLog(AuditLogType.AGV_TASK, task.getBillId(), task.getBillNo(), "取消AGV任务");
		});
	}

	@Override
	public void continueTask(List<Long> taskIdList) {
		List<WmsTask> wmsTaskList = wmsTaskDao.getTaskByIds(taskIdList);
		// 校验任务状态是否为异常中断，否则抛异常
		wmsTaskList.forEach(task -> {
			if (!WmsTaskStateEnum.ABNORMAL.getCode().equals(task.getTaskState().getCode())) {
				throw new ServiceException("继续执行任务失败，任务状态只能是：异常中断中");
			}
		});
		agvTask.continueTask(wmsTaskList);
		wmsTaskList.forEach(task -> {
			logBiz.auditLog(AuditLogType.AGV_TASK, task.getBillId(), task.getBillNo(), "继续执行AGV任务");
		});
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
