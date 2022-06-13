
package org.nodes.wms.core.basedata.service.impl;

import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuInstock;
import org.nodes.wms.core.basedata.mapper.SkuInstockMapper;
import org.nodes.wms.core.basedata.service.ISkuInstockService;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * 物品入库设置 服务实现类
 *
 * @author pengwei
 * @since 2019-12-23
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SkuInstockServiceImpl<M extends SkuInstockMapper, T extends SkuInstock>
	extends BaseServiceImpl<SkuInstockMapper, SkuInstock>
	implements ISkuInstockService {


	@Override
	public boolean save(SkuInstock entity) {
		if (!this.validData(entity)) {
			return false;
		}
		boolean result = super.save(entity);
		if (result) {
			//super.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean saveBatch(Collection<SkuInstock> entityList, int batchSize) {
		boolean result =  super.saveBatch(entityList, batchSize);
		if (result) {
			//super.saveOrUpdateBatch(entityList);
		}
		return result;
	}

	@Override
	public boolean updateById(SkuInstock entity) {
		if (!this.validData(entity)) {
			return false;
		}
		boolean result = super.updateById(entity);
		if (result) {
			//super.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean removeById(Serializable id) {
		boolean result = super.removeById(id);
		if (result) {
			//SkuInstockCache.remove((Long) id);
		}
		return result;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = super.removeByIds(idList);
		if (result) {
			//super.remove(idList);
		}
		return result;
	}

	private boolean validData(SkuInstock skuInstock) {
		//SkuInstock findData = SkuInstockCache.getOne(skuInstock.getSkuId(), skuInstock.getWhId());
		SkuInstock findData = super.list(Condition.getQueryWrapper(new SkuInstock())
		.lambda()
		.eq(SkuInstock::getSkuId,skuInstock.getSkuId())
		.eq(SkuInstock::getWhId,skuInstock.getWhId())
		).stream().findFirst().orElse(null);
		if (ObjectUtil.isNotEmpty(findData) && !findData.getWsiId().equals(skuInstock.getWsiId())) {
			Sku sku = SkuCache.getById(skuInstock.getSkuId());
			if (Func.isEmpty(sku)) {
				throw new ServiceException(String.format("物品[ID:%s]不存在! ", skuInstock.getSkuId()));
			}
			Warehouse warehouse = WarehouseCache.getById(skuInstock.getWhId());
			if (Func.isEmpty(warehouse)) {
				throw new ServiceException(String.format("库房[ID:%s]不存在! ", skuInstock.getWhId()));
			}
			throw new ServiceException(
				String.format(
					"物品[编码:%s, 名称:%s], 库房[编码:%s, 名称:%s] 对应的入库设置已存在",
					sku.getSkuCode(), sku.getSkuName(), warehouse.getWhCode(), warehouse.getWhName()));
		}
		return true;
	}
}
