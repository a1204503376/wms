package org.nodes.wms.dao.basics.sku.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.nodes.wms.dao.basics.sku.SkuTypeDao;
import org.nodes.wms.dao.basics.sku.entities.SkuType;
import org.nodes.wms.dao.basics.sku.mapper.SkuTypeMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 物品分类 DAO实现类
 */
@Repository
public class SkuTypeDaoImpl extends BaseServiceImpl<SkuTypeMapper, SkuType>
	implements SkuTypeDao {

	@Override
	public SkuType getSkuTypeById(Long skuTypeId) {
		return super.getById(skuTypeId);
	}

	@Override
	public SkuType getSkuTypeByCode(String typeCode) {
		return super.getOne(new LambdaQueryWrapper<SkuType>().eq(SkuType::getTypeCode, typeCode));
	}
}
