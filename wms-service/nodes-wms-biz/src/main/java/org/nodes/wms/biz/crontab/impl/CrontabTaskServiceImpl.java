package org.nodes.wms.biz.crontab.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.common.log.impl.LogBizImpl;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.crontab.CrontabTaskDao;
import org.nodes.wms.dao.crontab.entity.CrontabTask;
import org.nodes.wms.dao.crontab.mapper.CrontabTaskMapper;
import org.nodes.wms.biz.crontab.ICrontabTaskService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 任务表 服务实现类
 *
 * @author NodeX
 * @since 2021-01-22
 */
@Service
@Primary
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class CrontabTaskServiceImpl<M extends CrontabTaskMapper, T extends CrontabTask>
	extends BaseServiceImpl<CrontabTaskMapper, CrontabTask>
	implements ICrontabTaskService {

	private final LogBiz logBiz;

	private final CrontabTaskDao crontabTaskDao;
	@Override
	public boolean save(CrontabTask entity) {
		boolean result = super.save(entity);
		if (result) {
			//CrontabTaskCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean updateById(CrontabTask entity) {
		boolean result = super.updateById(entity);
		if (result) {
			//CrontabTaskCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean removeById(Serializable id) {
		boolean result = super.removeById(id);
		if (result) {
			//CrontabTaskCache.removeById((Long) id);
		}
		return result;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = super.removeByIds(idList);
		if (result) {
			//CrontabTaskCache.removeByIds(idList);
		}
		return result;
	}

	@Override
	public List<CrontabTask> getCrontabTaskList() {
		return super.list(new LambdaQueryWrapper<CrontabTask>().eq(CrontabTask::getEnabled,1));
	}


	@Override
	public void startCrontabTask(Long taskId) {
		crontabTaskDao.editEnabledById(taskId,1);
	}

	@Override
	public void stopCrontabTask(Long taskId) {
		crontabTaskDao.editEnabledById(taskId,0);
	}

	@Override
	public void logOfCrontabTask(String taskName, String log) {
        CrontabTask crontabTask = crontabTaskDao.getCrontabTaskByName(taskName);
		logBiz.auditLog("系统",AuditLogType.CRON_TASK,crontabTask.getId(),log);
	}

	@Override
	public boolean newCrontabTask(CrontabTask crontabTask) {
		super.saveOrUpdate(crontabTask);
		logBiz.auditLog(AuditLogType.CRON_TASK,crontabTask.getId(),"新增定时任务");
		return  true;
	}

	@Override
	public boolean editCrontabTask(CrontabTask crontabTask) {
		 super.saveOrUpdate(crontabTask);
		logBiz.auditLog(AuditLogType.CRON_TASK,crontabTask.getId(),"编辑定时任务");
		return true;
	}

	@Override
	@Transactional
	public boolean deleteByIds(List<Long> ids) {
		for (Long id : ids) {
			super.removeById(id);
			logBiz.auditLog(AuditLogType.CRON_TASK, id, "删除定时任务");
		}
	  return true;
	}

}
