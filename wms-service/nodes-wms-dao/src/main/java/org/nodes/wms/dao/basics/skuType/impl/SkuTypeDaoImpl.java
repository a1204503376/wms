package org.nodes.wms.dao.basics.skuType.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.wms.dao.basics.skuType.SkuTypeDao;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.nodes.wms.dao.basics.skuType.mapper.SkuTypeMapper;
import org.springblade.core.mp.base.BaseEntity;
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

    @Override
    public SkuType selectSkuTypeByCodeAndWoId(String typeCode, Long woId) {
		LambdaQueryWrapper<SkuType> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper
			.eq(SkuType::getTypeCode,typeCode)
			.eq(SkuType::getWoId,woId)
			.eq(BaseEntity::getIsDeleted,0);
        return super.getOne(queryWrapper);
    }

	@Override
	public SkuType saveSkuType(SkuType skuType) {
		super.saveOrUpdate(skuType);
		return skuType;
	}

	@Override
	public void updateByTypeId(SkuType skuType) {
		super.updateById(skuType);
	}
}
