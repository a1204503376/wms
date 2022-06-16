package org.nodes.wms.dao.basics.sku.impl;

import org.nodes.wms.dao.basics.sku.SkuIncDao;
import org.nodes.wms.dao.basics.sku.entities.SkuInc;
import org.nodes.wms.dao.basics.sku.mapper.SkuIncMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 物品关联供应商 DAO实现类
 */
@Repository
public class SkuIncDaoImpl extends BaseServiceImpl<SkuIncMapper, SkuInc>
	implements SkuIncDao {

	@Override
	public void saveBatchSkuInc(List<SkuInc> skuIncList) {
		super.saveBatch(skuIncList);
	}

	@Override
	public void deleteByIdList(List<Long> skuIncIdList) {
		super.deleteLogic(skuIncIdList);
	}
}
