package org.nodes.wms.dao.crontab.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.crontab.CrontabTaskDao;
import org.nodes.wms.dao.crontab.entity.CrontabTask;
import org.nodes.wms.dao.crontab.mapper.CrontabTaskMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 任务表 DAO 实现类
 */
@Repository
@RequiredArgsConstructor
public class CrontabTaskDaoImpl extends BaseServiceImpl<CrontabTaskMapper, CrontabTask> implements CrontabTaskDao {
	@Override
	public void editEnabledById(Long taskId, int num) {
		super.update(new LambdaUpdateWrapper<CrontabTask>()
			.set(CrontabTask::getEnabled,num)
			.eq(CrontabTask::getId,taskId));
	}

	@Override
	public CrontabTask getCrontabTaskByName(String taskName) {
		return super.getOne(new LambdaQueryWrapper<CrontabTask>().eq(CrontabTask::getCrontabTaskName,taskName));
	}
}
