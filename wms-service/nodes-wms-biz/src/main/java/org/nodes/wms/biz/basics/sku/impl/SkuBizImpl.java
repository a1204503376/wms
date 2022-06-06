package org.nodes.wms.biz.basics.sku.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.dao.basics.sku.SkuDao;
import org.nodes.wms.dao.basics.sku.SkuTypeDao;
import org.nodes.wms.dao.basics.sku.SkuUmDao;
import org.nodes.wms.dao.basics.sku.dto.SkuSelectQuery;
import org.nodes.wms.dao.basics.sku.dto.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.dto.SkuUmSelectResponse;
import org.nodes.wms.dao.basics.sku.entities.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 物品 业务层实现类
 */
@Service
@RequiredArgsConstructor
public class SkuBizImpl implements SkuBiz {

	private final SkuDao skuDao;
	private final SkuTypeDao skuTypeDao;
	private final SkuUmDao skuUmDao;

	@Override
	public List<SkuSelectResponse> getSkuSelectResponseTop10List(SkuSelectQuery skuSelectQuery) {
		return skuDao.listTop10BySkuCodeSkuName(skuSelectQuery.getKey(), skuSelectQuery.getKey());
	}

    @Override
    public Sku findById(Long skuId) {
        return skuDao.getById(skuId);
    }

    @Override
    public Sku findByCode(String skuCode) {
		return skuDao.getSkuByCode(skuCode);
    }

	@Override
	public SkuType findSkuTypeById(Long skuTypeId) {
		return  skuTypeDao.getSkuTypeById(skuTypeId);
	}

	@Override
	public SkuType findSkuTypeByCode(String typeCode) {
		return skuTypeDao.getSkuTypeByCode(typeCode);
	}

	@Override
    public List<SkuUmSelectResponse> findSkuUmSelectResponseListBySkuId(Long skuId) {
        return skuDao.listSkuUmBySkuId(skuId);
    }

    @Override
    public SkuPackageAggregate findSkuPackageAggregateBySkuId(Long skuId) {
        return skuDao.getSkuPackageAggregateBySkuId(skuId);
    }

	@Override
	public SkuUm findSkuUmByUmCode(String skuUmCode) {
		return skuDao.getSkuUmByUmCode(skuUmCode);
	}

	@Override
	public SkuUm findSkuUmById(Long wsuId) {
		return skuUmDao.getSkuUmById(wsuId);
	}

	@Override
    public SkuPackageDetail findBaseSkuPackageDetail(Long skuId) {
        return skuDao.getBaseSkuPackageDetail(skuId);
    }

}
