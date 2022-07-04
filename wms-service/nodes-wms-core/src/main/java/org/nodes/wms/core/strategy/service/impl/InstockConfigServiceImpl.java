
package org.nodes.wms.core.strategy.service.impl;

import org.nodes.wms.dao.putway.entities.InstockConfig;
import org.nodes.wms.core.strategy.mapper.InstockConfigMapper;
import org.nodes.wms.core.strategy.service.IInstockConfigService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * 上架策略执行条件 服务实现类
 *
 * @author chz
 * @since 2020-02-14
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class InstockConfigServiceImpl<M extends InstockConfigMapper, T extends InstockConfig>
	extends BaseServiceImpl<InstockConfigMapper, InstockConfig>
	implements IInstockConfigService {

	@Override
	public boolean save(InstockConfig entity) {
		boolean result = super.save(entity);
		if (result) {
			//InstockConfigCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean saveBatch(Collection<InstockConfig> entityList, int batchSize) {
		boolean result = super.saveBatch(entityList, batchSize);
		if (result) {
			//entityList.forEach(instockConfig->InstockConfigCache.saveOrUpdate(instockConfig));
		}
		return result;
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<InstockConfig> entityList, int batchSize) {
		boolean result =  super.saveOrUpdateBatch(entityList, batchSize);
		if (result) {
			//entityList.forEach(instockConfig -> InstockConfigCache.saveOrUpdate(instockConfig));
		}
		return result;
	}

	@Override
	public boolean updateById(InstockConfig entity) {
		boolean result = super.updateById(entity);
		if (result) {
			//InstockConfigCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean removeById(Serializable id) {
		boolean result = super.removeById(id);
		if (result) {
			//InstockConfigCache.remove((Long) id);
		}
		return result;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = super.removeByIds(idList);
		if (result) {
			//InstockConfigCache.remove(idList);
		}
		return result;
	}
}
