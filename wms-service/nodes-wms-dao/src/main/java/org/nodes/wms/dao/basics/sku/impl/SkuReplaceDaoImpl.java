package org.nodes.wms.dao.basics.sku.impl;

import org.nodes.wms.dao.basics.sku.SkuReplaceDao;
import org.nodes.wms.dao.basics.sku.SkuUmDao;
import org.nodes.wms.dao.basics.sku.entities.SkuReplace;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;
import org.nodes.wms.dao.basics.sku.mapper.SkuReplaceMapper;
import org.nodes.wms.dao.basics.sku.mapper.SkuUmMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 物品替代 DAO实现类
 */
@Repository
public class SkuReplaceDaoImpl extends BaseServiceImpl<SkuReplaceMapper, SkuReplace>
	implements SkuReplaceDao {

	@Override
	public void saveBatchSkuReplace(List<SkuReplace> skuReplaceList) {
		super.saveBatch(skuReplaceList);
	}

	@Override
	public void deleteByIdList(List<Long> skuReplaceIdList) {
		super.deleteLogic(skuReplaceIdList);
	}
}
