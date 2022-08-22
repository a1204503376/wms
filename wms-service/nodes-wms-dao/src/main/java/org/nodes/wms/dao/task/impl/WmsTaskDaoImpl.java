package org.nodes.wms.dao.task.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.task.WmsTaskDao;
import org.nodes.wms.dao.task.dto.input.TaskPageQuery;
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
	public void updateState(Long taskId, WmsTaskStateEnum state) {
		UpdateWrapper<WmsTask> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
			.eq(WmsTask::getTaskId, taskId)
			.set(WmsTask::getTaskState, state.getCode());

		if (WmsTaskStateEnum.COMPLETED.equals(state)
			|| WmsTaskStateEnum.CANCELED.equals(state)) {
			updateWrapper.lambda().set(WmsTask::getCloseTime, LocalDateTime.now());
		} else if (WmsTaskStateEnum.ISSUED.equals(state)) {
			updateWrapper.lambda().set(WmsTask::getAllotTime, LocalDateTime.now());
		} else if (WmsTaskStateEnum.START_EXECUTION.equals(state)) {
			updateWrapper.lambda().set(WmsTask::getBeginTime, LocalDateTime.now());
		}

		if (!super.update(updateWrapper)) {
			throw new ServiceException("更新任务状态失败");
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
	public WmsTask findTaskByBoxCode(String boxCode, WmsTaskProcTypeEnum taskProcTypeEnum) {
		LambdaQueryChainWrapper<WmsTask> lambdaQuery = super.lambdaQuery()
			.eq(WmsTask::getBoxCode, boxCode)
			.eq(WmsTask::getTaskTypeCd, WmsTaskTypeEnum.PICKING)
			.in(WmsTask::getTaskState, WmsTaskStateEnum.NOT_ISSUED, WmsTaskStateEnum.ISSUED, WmsTaskStateEnum.START_EXECUTION, WmsTaskStateEnum.ABNORMAL)
			.apply("task_qty <> scan_qty");
		if (Func.isEmpty(taskProcTypeEnum)) {
			lambdaQuery.in(WmsTask::getTaskProcType, WmsTaskProcTypeEnum.BY_LOC,
				WmsTaskProcTypeEnum.BY_BOX, WmsTaskProcTypeEnum.BY_PCS, WmsTaskProcTypeEnum.BY_LPN);
		} else {
			lambdaQuery.eq(WmsTask::getTaskProcType, taskProcTypeEnum);
		}
		AssertUtil.notNull(boxCode, "根据箱码获取任务失败，箱码为空");

		List<WmsTask> wmsTaskList = lambdaQuery.list();
		AssertUtil.notNull(wmsTaskList, "根据箱码获取任务失败，此箱码不存在任务");
		if (wmsTaskList.size() > 1 || wmsTaskList.size() == 0) {
			throw new ServiceException("根据箱码获取任务失败，查询出多个任务，或查询不到跟此箱码相关的任务");
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
			throw new ServiceException("任务更新失败,请再次重试");
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
}
