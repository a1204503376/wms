package org.nodes.wms.dao.basics.sku.impl;

import org.nodes.wms.dao.basics.sku.SkuUmDao;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;
import org.nodes.wms.dao.basics.sku.mapper.SkuUmMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 计量单位 DAO实现类
 */
@Repository
public class SkuUmDaoImpl extends BaseServiceImpl<SkuUmMapper, SkuUm>
	implements SkuUmDao {
	@Override
	public SkuUm getSkuUmById(Long wsuId) {
		return super.getById(wsuId);
	}
}
