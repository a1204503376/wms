package org.nodes.wms.dao.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.task.WmsTaskDao;
import org.nodes.wms.dao.task.dto.input.TaskPageQuery;
import org.nodes.wms.dao.task.dto.output.TaskExcelResponse;
import org.nodes.wms.dao.task.dto.output.TaskPageResponse;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskProcTypeEnum;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.nodes.wms.dao.task.enums.WmsTaskTypeEnum;
import org.nodes.wms.dao.task.mapper.WmsTaskMapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author nodesc
 */
@Repository
@RequiredArgsConstructor
public class WmsTaskDaoImpl
	extends BaseServiceImpl<WmsTaskMapper, WmsTask>
	implements WmsTaskDao {

	@Override
	public void updateState(Long taskId, WmsTaskStateEnum state, String msg) {
		UpdateWrapper<WmsTask> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
			.eq(WmsTask::getTaskId, taskId);

		WmsTask wmsTask = new WmsTask();
		wmsTask.setTaskState(state);
		if (Func.isNotEmpty(msg)) {
			wmsTask.setRemark(msg);
		}
		if (WmsTaskStateEnum.AGV_COMPLETED.equals(state)) {
			wmsTask.setCloseTime(LocalDateTime.now());
		} else if (WmsTaskStateEnum.ISSUED.equals(state)) {
			wmsTask.setAllotTime(LocalDateTime.now());
		} else if (WmsTaskStateEnum.START_EXECUTION.equals(state)) {
			wmsTask.setBeginTime(LocalDateTime.now());
		} else if (WmsTaskStateEnum.AGV_RECEIVED.equals(state)) {
			wmsTask.setConfirmDate(LocalDateTime.now());
		}

		if (!super.update(wmsTask, updateWrapper)) {
			throw new ServiceException("????????????????????????,???????????????");
		}
	}

	@Override
	public Page<TaskPageResponse> getPage(IPage<Object> page, TaskPageQuery taskPageQuery) {
		return super.baseMapper.getPage(page, taskPageQuery);
	}

	@Override
	public List<WmsTask> getTaskByIds(List<Long> taskIdList) {
		return super.listByIds(taskIdList);
	}

	@Override
	public List<WmsTask> getTaskByState(int taskState) {
		return super.baseMapper.getTaskByState(taskState);
	}

	@Override
	public WmsTask findPickTaskByBoxCode(String boxCode, WmsTaskProcTypeEnum taskProcTypeEnum, String lot) {
		AssertUtil.notNull(boxCode, "?????????????????????????????????????????????");

		LambdaQueryChainWrapper<WmsTask> lambdaQuery = super.lambdaQuery()
			.eq(WmsTask::getBoxCode, boxCode)
			.in(WmsTask::getTaskTypeCd, WmsTaskTypeEnum.PICKING, WmsTaskTypeEnum.AGV_PICKING)
			.in(WmsTask::getTaskState, WmsTaskStateEnum.NOT_ISSUED, WmsTaskStateEnum.ISSUED,
				WmsTaskStateEnum.START_EXECUTION, WmsTaskStateEnum.ABNORMAL, WmsTaskStateEnum.AGV_COMPLETED)
			.apply("task_qty <> scan_qty");
		if (Func.isNotEmpty(taskProcTypeEnum)) {
			lambdaQuery.eq(WmsTask::getTaskProcType, taskProcTypeEnum);
		}
		if (Func.isNotEmpty(lot)) {
			lambdaQuery.eq(WmsTask::getLot, lot);
		}

		List<WmsTask> wmsTaskList = lambdaQuery.list();
		if (Func.isNotEmpty(taskProcTypeEnum) && wmsTaskList.size() == 0 && taskProcTypeEnum == WmsTaskProcTypeEnum.BY_PCS) {
			return null;
		}
		AssertUtil.notNull(wmsTaskList, "?????????????????????????????????????????????????????????");
		if (wmsTaskList.size() > 1) {
			throw new ServiceException("??????????????????????????????????????????????????????");
		} else if (wmsTaskList.size() == 0) {
			throw new ServiceException("???????????????????????????????????????????????????");
		}
		return wmsTaskList.get(0);
	}

	@Override
	public void updateWmsTaskStateByTaskId(Long taskId, WmsTaskStateEnum taskStateEnum, BigDecimal scanQty) {
		UpdateWrapper<WmsTask> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
			.eq(WmsTask::getTaskId, taskId);
		WmsTask wmsTask = new WmsTask();
		wmsTask.setTaskState(taskStateEnum);
		wmsTask.setScanQty(scanQty);
		if (!super.update(wmsTask, updateWrapper)) {
			throw new ServiceException("??????????????????,???????????????");
		}
	}

	@Override
	public WmsTask getEnableTaskBySoBillId(Long soBillId, Long soDetailId) {
		return super.lambdaQuery()
			.eq(WmsTask::getBillId, soBillId)
			.eq(WmsTask::getBillDetailId, soDetailId)
			.eq(WmsTask::getTaskTypeCd, WmsTaskTypeEnum.PICKING)
			.in(WmsTask::getTaskState, WmsTaskStateEnum.NOT_ISSUED, WmsTaskStateEnum.ISSUED, WmsTaskStateEnum.START_EXECUTION, WmsTaskStateEnum.ABNORMAL)
			.last("limit 1")
			.one();
	}

	@Override
	public void updateWmsTaskByPartParam(Long taskId, WmsTaskProcTypeEnum taskProcTypeEnum, Location toLocation, String boxCode) {
		UpdateWrapper<WmsTask> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
			.eq(WmsTask::getTaskId, taskId);
		WmsTask wmsTask = new WmsTask();
		wmsTask.setTaskProcType(taskProcTypeEnum);
		wmsTask.setToLocId(toLocation.getLocId());
		wmsTask.setToLocCode(toLocation.getLocCode());
		if (Func.isNotEmpty(boxCode)) {
			wmsTask.setBoxCode(boxCode);
		}
		if (!super.update(wmsTask, updateWrapper)) {
			throw new ServiceException("??????????????????,???????????????");
		}
	}

	@Override
	public void updateRemark(Long taskId, String remark) {
		if (Func.isEmpty(remark)) {
			remark = "";
		}
		UpdateWrapper<WmsTask> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
			.eq(WmsTask::getTaskId, taskId);
		WmsTask wmsTask = new WmsTask();
		wmsTask.setRemark(remark);
		if (!super.update(wmsTask, updateWrapper)) {
			throw new ServiceException("??????????????????,???????????????");
		}
	}

	@Override
	public List<TaskExcelResponse> getListForExport(TaskPageQuery queryParam) {
		LambdaQueryWrapper<WmsTask> queryWrapper = Wrappers.lambdaQuery(WmsTask.class);
		queryWrapper
			.like(Func.isNotBlank(queryParam.getTaskId()), WmsTask::getTaskId, queryParam.getTaskId())
			.like(Func.isNotBlank(queryParam.getBillNo()), WmsTask::getBillNo, queryParam.getBillNo())
			.in(Func.isNotEmpty(queryParam.getTaskTypeCdList()), WmsTask::getTaskTypeCd, queryParam.getTaskTypeCdList())
			.in(Func.isNotEmpty(queryParam.getTaskStateList()), WmsTask::getTaskState, queryParam.getTaskStateList())
			.like(Func.isNotBlank(queryParam.getSkuCode()), WmsTask::getSkuCode, queryParam.getSkuCode())
			.like(Func.isNotBlank(queryParam.getFromLocCode()), WmsTask::getFromLocCode, queryParam.getFromLocCode())
			.like(Func.isNotBlank(queryParam.getToLocCode()), WmsTask::getToLocCode, queryParam.getToLocCode())
			.like(Func.isNotBlank(queryParam.getBoxCode()), WmsTask::getBoxCode, queryParam.getBoxCode());
//				.eq(WmsTask::getIsDeleted, 0);
		return super.baseMapper.listForExport(queryWrapper);
	}

	@Override
	public List<WmsTask> getOldTaskByNewTask(WmsTask task) {
		LambdaQueryChainWrapper<WmsTask> lambdaQuery = super.lambdaQuery()
			.eq(WmsTask::getBoxCode, task.getBoxCode())
			.eq(WmsTask::getTaskTypeCd, task.getTaskTypeCd())
			.in(WmsTask::getTaskState, WmsTaskStateEnum.NOT_ISSUED, WmsTaskStateEnum.ISSUED,
				WmsTaskStateEnum.START_EXECUTION, WmsTaskStateEnum.AGV_RECEIVED, WmsTaskStateEnum.AGV_ASSIGNED)
			.apply("task_qty <> scan_qty");
		return lambdaQuery.list();
	}
}
