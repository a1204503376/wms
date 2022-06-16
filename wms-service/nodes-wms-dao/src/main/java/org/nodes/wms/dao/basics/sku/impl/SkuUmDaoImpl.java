package org.nodes.wms.dao.basics.sku.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.nodes.wms.dao.basics.customer.entities.BasicsCustomer;
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

    @Override
    public boolean isExistUmCode(String wsuCode) {
		LambdaQueryWrapper<SkuUm> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(SkuUm::getWsuCode,wsuCode);
		int count = super.count(lambdaQueryWrapper);
		return count>0;

    }

	@Override
	public void update(SkuUm skuUm) {
		super.update(new LambdaUpdateWrapper<SkuUm>()
			.set(SkuUm::getWsuCode,skuUm.getWsuCode())
			.eq(SkuUm::getWsuId,skuUm.getWsuCode()));
	}

	@Override
	public void insert(SkuUm skuUm) {
		super.save(skuUm);
	}
}
