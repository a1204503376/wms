
package org.nodes.wms.core.strategy.service.impl;

import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.strategy.cache.InstockConfigLotCache;
import org.nodes.wms.core.strategy.dto.InstockConfigLotDTO;
import org.nodes.wms.core.strategy.entity.InstockConfigLot;
import org.nodes.wms.core.strategy.mapper.InstockConfigLotMapper;
import org.nodes.wms.core.strategy.service.IInstockConfigLotService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 *  上架策略批属性设定 服务实现类
 *
 * @author NodeX
 * @since 2019-12-09
 */
@Service
@Primary
@Transactional(propagation= Propagation.NESTED,isolation= Isolation.DEFAULT,rollbackFor=Exception.class)
public class InstockConfigLotServiceImpl<M extends InstockConfigLotMapper, T extends InstockConfigLot>
	extends BaseServiceImpl<InstockConfigLotMapper, InstockConfigLot>
	implements IInstockConfigLotService {

	@Override
	public boolean save(InstockConfigLot entity) {
		boolean result = super.save(entity);
		if (result) {
		//	InstockConfigLotCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean saveBatch(Collection<InstockConfigLot> entityList, int batchSize) {
		boolean result = super.saveBatch(entityList, batchSize);
		if (result) {
		//	entityList.forEach(instockConfigLot -> InstockConfigLotCache.saveOrUpdate(instockConfigLot));
		}
		return result;
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<InstockConfigLot> entityList, int batchSize) {
		boolean result =  super.saveOrUpdateBatch(entityList, batchSize);
		if (result) {
		//	entityList.forEach(instockConfigLot -> InstockConfigLotCache.saveOrUpdate(instockConfigLot));
		}
		return result;
	}

	@Override
	public boolean updateById(InstockConfigLotDTO instockConfigLotDTO) {
		boolean result = this.updateById((InstockConfigLot) instockConfigLotDTO);
		if (result) {
		//	InstockConfigLotCache.saveOrUpdate(instockConfigLotDTO);
		}
		return result;
	}

	@Override
	public boolean removeById(Serializable id) {
		boolean result = super.removeById(id);
		if (result) {
		//	InstockConfigLotCache.remove(id);
		}
		return result;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = super.removeByIds(idList);
		if (result) {
		//	InstockConfigLotCache.remove(idList);
		}
		return result;
	}

	public boolean match(List<InstockConfigLot> instockConfigLotList, SkuLotBaseEntity skuLotEntity) {
		List<Boolean> result = new ArrayList<>();
		for (InstockConfigLot instockConfigLot : instockConfigLotList) {
			// 获取批属性的值
			String value = skuLotEntity.skuLotGet(instockConfigLot.getSkuLotNumber());
			if (Func.isEmpty(value)) {
				continue;
			}
			int operator = instockConfigLot.getSkuLotOperation();
			boolean matchResult = NodesUtil.match(value, instockConfigLot.getSkuLotVal1(), operator) ||
				NodesUtil.match(value, instockConfigLot.getSkuLotVal2(), operator) ||
				NodesUtil.match(value, instockConfigLot.getSkuLotVal3(), operator);
			result.add(matchResult);
		}
		return !result.contains(false);
	}
}
