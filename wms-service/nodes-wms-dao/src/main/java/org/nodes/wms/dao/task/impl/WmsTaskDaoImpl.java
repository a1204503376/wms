package org.nodes.wms.dao.task.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.task.WmsTaskDao;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.nodes.wms.dao.task.mapper.WmsTaskMapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;

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
		if (!super.update(updateWrapper)) {
			throw new ServiceException("更新任务状态失败");
		}
	}

}
