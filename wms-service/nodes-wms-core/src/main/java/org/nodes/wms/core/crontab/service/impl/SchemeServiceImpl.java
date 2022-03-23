package org.nodes.wms.core.crontab.service.impl;

import lombok.AllArgsConstructor;
import org.nodes.wms.core.crontab.cache.SchemeCache;
import org.nodes.wms.core.crontab.entity.Scheme;
import org.nodes.wms.core.crontab.mapper.SchemeMapper;
import org.nodes.wms.core.crontab.service.ISchemeService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * 任务计划表 服务实现类
 *
 * @author NodeX
 * @since 2021-01-22
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SchemeServiceImpl<M extends SchemeMapper, T extends Scheme>
	extends BaseServiceImpl<SchemeMapper, Scheme>
	implements ISchemeService {

	@Override
	public boolean save(Scheme entity) {
		boolean result = super.save(entity);
		if (result) {
		//	SchemeCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean updateById(Scheme entity) {
		boolean result = super.updateById(entity);
		if (result) {
		//	SchemeCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean removeById(Serializable id) {
		boolean result = super.removeById(id);
		if (result) {
		//	SchemeCache.removeById((Long)id);
		}
		return result;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = super.removeByIds(idList);
		if (result) {
		//	SchemeCache.removeByIds(idList);
		}
		return result;
	}
}
