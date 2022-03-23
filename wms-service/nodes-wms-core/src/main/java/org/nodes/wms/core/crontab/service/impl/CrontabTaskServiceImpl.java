package org.nodes.wms.core.crontab.service.impl;

import lombok.AllArgsConstructor;
import org.nodes.wms.core.crontab.cache.CrontabTaskCache;
import org.nodes.wms.core.crontab.entity.CrontabTask;
import org.nodes.wms.core.crontab.mapper.CrontabTaskMapper;
import org.nodes.wms.core.crontab.service.ICrontabTaskService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * 任务表 服务实现类
 *
 * @author NodeX
 * @since 2021-01-22
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class CrontabTaskServiceImpl<M extends CrontabTaskMapper, T extends CrontabTask>
	extends BaseServiceImpl<CrontabTaskMapper, CrontabTask>
	implements ICrontabTaskService {

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
}
