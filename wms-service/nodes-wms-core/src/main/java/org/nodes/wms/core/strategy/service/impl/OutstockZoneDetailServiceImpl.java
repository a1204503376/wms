
package org.nodes.wms.core.strategy.service.impl;

import org.nodes.wms.core.strategy.cache.OutstockZoneDetailCache;
import org.nodes.wms.core.strategy.entity.OutstockZoneDetail;
import org.nodes.wms.core.strategy.mapper.OutstockZoneDetailMapper;
import org.nodes.wms.core.strategy.service.IOutstockZoneDetailService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * 分配策略货源库区 服务实现类
 *
 * @author pengwei
 * @since 2020-02-12
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class OutstockZoneDetailServiceImpl<M extends OutstockZoneDetailMapper, T extends OutstockZoneDetail>
	extends BaseServiceImpl<OutstockZoneDetailMapper, OutstockZoneDetail>
	implements IOutstockZoneDetailService {

	@Override
	public boolean save(OutstockZoneDetail entity) {
		boolean result = super.save(entity);
		if (result) {
			//OutstockZoneDetailCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean updateById(OutstockZoneDetail entity) {
		boolean result = super.updateById(entity);
		if (result) {
			//OutstockZoneDetailCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean removeById(Serializable id) {
		boolean result = super.removeById(id);
		if (result) {
			//OutstockZoneDetailCache.remove(id);
		}
		return result;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = super.removeByIds(idList);
		if (result) {
			//OutstockZoneDetailCache.remove(idList);
		}
		return result;
	}
}
