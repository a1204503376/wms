package org.nodes.wms.dao.task.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import org.springframework.stereotype.Repository;

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
	public WmsTask findTaskByBoxCode(String boxCode) {
		AssertUtil.notNull(boxCode, "根据箱码获取任务失败，任务为空");
		List<WmsTask> wmsTaskList = super.lambdaQuery()
			.eq(WmsTask::getBoxCode, boxCode)
			.eq(WmsTask::getTaskTypeCd, WmsTaskTypeEnum.PICKING)
			.eq(WmsTask::getTaskProcType, WmsTaskProcTypeEnum.BY_BOX)
			.in(WmsTask::getTaskState, WmsTaskStateEnum.NOT_ISSUED, WmsTaskStateEnum.ISSUED, WmsTaskStateEnum.START_EXECUTION, WmsTaskStateEnum.ABNORMAL)
			.last("pick_plan_qty <> pick_real_qty")
			.list();
		if (wmsTaskList.size() > 1) {
			throw new ServiceException("根据箱码获取任务失败，查询出多个任务，请检查任务后重试");
		}
		return wmsTaskList.get(0);
	}
}
