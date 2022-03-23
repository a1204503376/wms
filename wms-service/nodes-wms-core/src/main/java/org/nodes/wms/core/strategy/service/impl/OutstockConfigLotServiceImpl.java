
package org.nodes.wms.core.strategy.service.impl;

import org.nodes.wms.core.strategy.cache.OutstockConfigLotCache;
import org.nodes.wms.core.strategy.entity.OutstockConfigLot;
import org.nodes.wms.core.strategy.mapper.OutstockConfigLotMapper;
import org.nodes.wms.core.strategy.service.IOutstockConfigLotService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 *  分配策略批属性设定 服务实现类
 *
 * @author zhongls
 * @since 2019-12-10
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class OutstockConfigLotServiceImpl<M extends OutstockConfigLotMapper, T extends OutstockConfigLot>
	extends BaseServiceImpl<OutstockConfigLotMapper, OutstockConfigLot>
	implements IOutstockConfigLotService {

	@Override
	public boolean save(OutstockConfigLot entity) {
		boolean result = super.save(entity);
		if (result) {
			//OutstockConfigLotCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean updateById(OutstockConfigLot entity) {
		boolean result = super.updateById(entity);
		if (result) {
			//OutstockConfigLotCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean removeById(Serializable id) {
		boolean result = super.removeById(id);
		if (result) {
			//OutstockConfigLotCache.remove(id);
		}
		return result;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = super.removeByIds(idList);
		if (result) {
			//OutstockConfigLotCache.remove(idList);
		}
		return result;
	}
}
