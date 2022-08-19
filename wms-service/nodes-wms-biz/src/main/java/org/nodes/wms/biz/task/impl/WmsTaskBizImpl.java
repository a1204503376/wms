package org.nodes.wms.biz.task.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
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
import java.math.BigDecimal;
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

	@Override
	public void log(WmsTask wmsTask) {
		logBiz.auditLog(AuditLogType.CRON_TASK,
			wmsTask.getTaskId(),
			wmsTask.getBillNo(),
			String.format("任务ID[%s]:单据号[%s]来源库位[%s]目标库位[%s]任务编码[%s]任务状态[%s]",
				wmsTask.getTaskId(), wmsTask.getBillNo(), wmsTask.getFromLocId()
				, wmsTask.getToLocId(), wmsTask.getTaskTypeCd().getCode(), wmsTask.getTaskState().getCode()));
	}

	@Override
	public WmsTask findEnableTaskByBoxCode(String boxCode) {
		return wmsTaskDao.findTaskByBoxCode(boxCode);
	}

	@Override
	public void updateWmsTaskStateByTaskId(Long taskId, WmsTaskStateEnum taskStateEnum, BigDecimal scanQty) {
		AssertUtil.notNull(taskId, "修改任务状态失败,任务ID为空");
		AssertUtil.notNull(taskStateEnum, "修改任务状态失败,任务状态为空");
		AssertUtil.notNull(scanQty, "修改任务状态失败,实际拣货量为空");
		wmsTaskDao.updateWmsTaskStateByTaskId(taskId, taskStateEnum, scanQty);
	}

	@Override
	public WmsTask findEnableTaskBySoBillId(Long soBillId, Long soDetailId) {
		return null;
	}

}
