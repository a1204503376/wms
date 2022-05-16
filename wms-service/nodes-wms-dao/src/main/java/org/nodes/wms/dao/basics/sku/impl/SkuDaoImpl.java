package org.nodes.wms.dao.basics.sku.impl;

import org.nodes.wms.dao.basics.sku.SkuDao;
import org.nodes.wms.dao.basics.sku.dto.SkuPackageDetailResponse;
import org.nodes.wms.dao.basics.sku.dto.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.dto.SkuUmSelectResponse;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;
import org.nodes.wms.dao.basics.sku.mapper.SkuMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 物品 DAO实现类
 */
@Repository
public class SkuDaoImpl
	extends BaseServiceImpl<SkuMapper, Sku>
	implements SkuDao {

	@Override
	public List<SkuSelectResponse> listTop10BySkuCodeSkuName(String skuCode, String skuName) {
		return super.baseMapper.listTop10BySkuCodeSkuName(skuCode, skuName);
	}

    @Override
    public Sku getById(Long skuId) {
        return super.getById(skuId);
    }

    @Override
    public List<SkuUmSelectResponse> listSkuUmBySkuId(Long skuId) {
        return super.baseMapper.listSkuUmBySkuId(skuId);
    }

	@Override
	public List<SkuPackageDetailResponse> listSkuPackDetailBySkuId(Long skuId) {
		return super.baseMapper.listSkuPackDetailBySkuId(skuId);
	}

	@Override
	public SkuUm getSkuUmByUmCode(String skuUmCode) {

		return super.baseMapper.getSkuUmByUmCode(skuUmCode);
	}
}
