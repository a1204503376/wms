
package org.nodes.wms.core.strategy.service.impl;

import org.nodes.wms.core.strategy.cache.OutstockConfigCache;
import org.nodes.wms.core.strategy.entity.OutstockConfig;
import org.nodes.wms.core.strategy.mapper.OutstockConfigMapper;
import org.nodes.wms.core.strategy.service.IOutstockConfigService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * 分配策略执行条件 服务实现类
 *
 * @author pengwei
 * @since 2020-02-12
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class OutstockConfigServiceImpl<M extends OutstockConfigMapper, T extends OutstockConfig>
	extends BaseServiceImpl<OutstockConfigMapper, OutstockConfig>
	implements IOutstockConfigService {

	@Override
	public boolean save(OutstockConfig entity) {
		boolean result = super.save(entity);
		if (result) {
			//OutstockConfigCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean updateById(OutstockConfig entity) {
		boolean result = super.updateById(entity);
		if (result) {
			//OutstockConfigCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean removeById(Serializable id) {
		boolean result = super.removeById(id);
		if (result) {
			//OutstockConfigCache.remove((Long) id);
		}
		return result;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = super.removeByIds(idList);
		if (result) {
			//OutstockConfigCache.remove(idList);
		}
		return result;
	}
}
