package org.nodes.wms.dao.task.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.task.WmsTaskDao;
import org.nodes.wms.dao.task.dto.input.TaskPageQuery;
import org.nodes.wms.dao.task.dto.output.TaskPageResponse;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
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
}
