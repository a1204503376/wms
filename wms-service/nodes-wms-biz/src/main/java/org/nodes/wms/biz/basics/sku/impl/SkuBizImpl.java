package org.nodes.wms.biz.basics.sku.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.dao.basics.sku.SkuDao;
import org.nodes.wms.dao.basics.sku.dto.*;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 物品 业务层实现类
 */
@Service
@RequiredArgsConstructor
public class SkuBizImpl implements SkuBiz {

	private final SkuDao skuDao;

	@Override
	public List<SkuSelectResponse> getSkuSelectResponseTop10List(SkuSelectQuery skuSelectQuery) {
		return skuDao.listTop10BySkuCodeSkuName(skuSelectQuery.getKey(), skuSelectQuery.getKey());
	}

    @Override
    public Sku findById(Long skuId) {
        return skuDao.getById(skuId);
    }

    @Override
    public List<SkuUmSelectResponse> getSkuUmSelectResponseListBySkuId(SkuUmSelectQuery skuUmSelectQuery) {
        return skuDao.listSkuUmBySkuId(skuUmSelectQuery.getSkuId());
    }

    @Override
    public List<SkuPackageDetailResponse> getSkuPackDetailListBySkuId(SkuPackageDetailQuery skuPackageDetailQuery) {
        return skuDao.listSkuPackDetailBySkuId(skuPackageDetailQuery.getSkuId());
    }

	@Override
	public SkuUm findSkuUmByUmCode(String skuUmCode) {
		return skuDao.getSkuUmByUmCode(skuUmCode);
	}

}
