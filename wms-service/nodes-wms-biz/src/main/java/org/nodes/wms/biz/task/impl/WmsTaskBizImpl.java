package org.nodes.wms.biz.task.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.outstock.logSoPick.LogSoPickBiz;
import org.nodes.wms.biz.outstock.plan.SoPickPlanBiz;
import org.nodes.wms.biz.outstock.so.SoBillBiz;
import org.nodes.wms.biz.task.AgvTask;
import org.nodes.wms.biz.task.WmsTaskBiz;
import org.nodes.wms.biz.task.factory.WmsTaskFactory;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.task.TaskDetailDao;
import org.nodes.wms.dao.task.WmsTaskDao;
import org.nodes.wms.dao.task.dto.input.StopTaskRequest;
import org.nodes.wms.dao.task.dto.input.TaskPageQuery;
import org.nodes.wms.dao.task.dto.output.TaskDetailExcelResponse;
import org.nodes.wms.dao.task.dto.output.TaskPageResponse;
import org.nodes.wms.dao.task.entities.TaskDetail;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskProcTypeEnum;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.nodes.wms.dao.task.enums.WmsTaskTypeEnum;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	private final WmsTaskFactory wmsTaskFactory;
	private final SoPickPlanBiz soPickPlanBiz;
	private final LogSoPickBiz logSoPickBiz;
	private final SoBillBiz soBillBiz;

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
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void cancel(List<Long> taskIdList) {
		List<WmsTask> wmsTaskList = wmsTaskDao.getTaskByIds(taskIdList);
		wmsTaskList.forEach(task -> {
			if (!WmsTaskStateEnum.ABNORMAL.getCode().equals(task.getTaskState().getCode())) {
				throw new ServiceException("取消任务失败，任务状态只能是：异常中断中");
			}
			// 取消任务
			agvTask.cancel(task);
			cancel(task);
			wmsTaskDao.updateState(task.getTaskId(), WmsTaskStateEnum.CANCELED, null);
			logBiz.auditLog(AuditLogType.AGV_TASK, task.getBillId(), task.getBillNo(), "取消AGV任务");
		});

	}

	@Override
	public void cancel(WmsTask task) {
		if (task.getTaskTypeCd().equals(WmsTaskTypeEnum.AGV_PICKING)) {
			SoHeader soHeader = soBillBiz.getSoHeaderById(task.getBillId());
			soPickPlanBiz.cancelPickPlan(task, soHeader);

			// 取消拣货任务或AGV拣货任务时，更新发货单状态
			if (WmsTaskTypeEnum.PICKING.equals(task.getTaskTypeCd()) || WmsTaskTypeEnum.AGV_PICKING.equals(task.getTaskTypeCd())){
				if (Func.isNotEmpty(logSoPickBiz.findEnableBySoHeaderId(task.getBillId()))) {
					soBillBiz.updateState(task.getBillId(), SoBillStateEnum.PART);
				} else {
					soBillBiz.updateState(task.getBillId(), SoBillStateEnum.CREATE);
				}
			}
		}
	}


	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void continueTask(List<Long> taskIdList) {
		List<WmsTask> wmsTaskList = wmsTaskDao.getTaskByIds(taskIdList);
		// 校验任务状态是否为异常中断，否则抛异常
		wmsTaskList.forEach(task -> {
			if (!WmsTaskStateEnum.ABNORMAL.getCode().equals(task.getTaskState().getCode())) {
				throw new ServiceException("继续执行任务失败，任务状态只能是：异常中断中");
			}
			agvTask.continueTask(task);
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
				wmsTask.getTaskId(), wmsTask.getBillNo(), wmsTask.getFromLocId(), wmsTask.getToLocId(),
				wmsTask.getTaskTypeCd().getCode(), wmsTask.getTaskState().getCode()));
	}

	@Override
	public WmsTask findPickTaskByBoxCode(String boxCode, WmsTaskProcTypeEnum taskProcTypeEnum, String lot) {
		return wmsTaskDao.findPickTaskByBoxCode(boxCode, taskProcTypeEnum, lot);
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
		return wmsTaskDao.getEnableTaskBySoBillId(soBillId, soDetailId);
	}

	@Override
	public WmsTask create(WmsTaskTypeEnum taskType, WmsTaskProcTypeEnum procType,
						  List<SoPickPlan> soPickPlanList, SoHeader soHeader) {
		BigDecimal planQty = soPickPlanList.stream()
			.map(SoPickPlan::getPickPlanQty)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		WmsTask wmsTask = wmsTaskFactory.create(WmsTaskStateEnum.ISSUED, taskType, procType,
			soPickPlanList, soHeader, planQty);
		if (!wmsTaskDao.save(wmsTask)) {
			throw new ServiceException("任务下发失败，保存任务失败");
		}

		return wmsTask;
	}

	@Override
	public void updateWmsTaskByPartParam(Long taskId, WmsTaskProcTypeEnum taskProcTypeEnum, Location toLocation, String boxCode) {
		AssertUtil.notNull(taskId, "更新任务失败，任务ID为空");
		AssertUtil.notNull(taskProcTypeEnum, "更新任务失败，任务执行方式为空");
		AssertUtil.notNull(toLocation, "更新任务失败， 来源库位为空");
		wmsTaskDao.updateWmsTaskByPartParam(taskId, taskProcTypeEnum, toLocation, boxCode);
	}

	@Override
	public WmsTask findByTaskId(Long taskId) {
		AssertUtil.notNull(taskId, "下发任务失败，任务ID为空");
		return wmsTaskDao.getById(taskId);
	}

	@Override
	public void updateTaskStateToIssued(Long taskId) {
		AssertUtil.notNull(taskId, "下发任务失败，修改任务状态时任务ID为空");
		wmsTaskDao.updateState(taskId, WmsTaskStateEnum.ISSUED, null);
	}

}
