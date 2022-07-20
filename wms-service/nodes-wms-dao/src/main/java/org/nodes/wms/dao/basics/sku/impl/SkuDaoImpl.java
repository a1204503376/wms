package org.nodes.wms.dao.basics.sku.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.basics.sku.SkuDao;
import org.nodes.wms.dao.basics.sku.dto.output.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.dto.output.SkuUmSelectResponse;
import org.nodes.wms.dao.basics.sku.entities.*;
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
	public SkuPackageAggregate getSkuPackageAggregateBySkuId(Long skuId) {
		return super.baseMapper.getSkuPackageAggregateBySkuId(skuId);
	}

	@Override
	public SkuUm getSkuUmByUmCode(String skuUmCode) {
		return super.baseMapper.getSkuUmByUmCode(skuUmCode);
	}

	@Override
	public SkuPackageDetail getBaseSkuPackageDetail(Long skuId) {
		return super.baseMapper.getBaseSkuPackageDetail(skuId);
	}

	@Override
	public Sku getSkuByCode(String skuCode) {
		return super.getOne(new LambdaQueryWrapper<Sku>().eq(Sku::getSkuCode, skuCode));
	}

	@Override
	public void saveSku(Sku sku) {
		super.saveOrUpdate(sku);
	}

	@Override
	public SkuPackage getSkuPackageByWspId(Long wspId) {
		return super.baseMapper.selectSkuPackageByWspId(wspId);
	}

	@Override
	public List<Sku> getSkuList() {
		QueryWrapper<Sku> skuQueryWrapper = new QueryWrapper<>();
		skuQueryWrapper.select("distinct sku_spec");
		skuQueryWrapper.isNotNull("sku_spec");
		return super.list(skuQueryWrapper);
	}

	@Override
	public int countByCode(String skuCode) {

		return super.count(new LambdaQueryWrapper<Sku>().eq(Sku::getSkuCode, skuCode));
	}

	@Override
	public SkuPackageDetail getSkuPackageDetailBySkuId(Long skuId, String wsuCode) {
		return super.baseMapper.getSkuPackageDetailBySkuId(skuId, wsuCode);
	}

	@Override
	public List<Sku> getSkuListByNo(String no) {
		AssertUtil.notNull(no, "获取SKU集合失败,原因物料编码/物料型号为空");
		LambdaQueryWrapper<Sku> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(Sku::getSkuSpec, no)
			.or()
			.eq(Sku::getSkuCode, no);
		return super.list(lambdaQueryWrapper);
	}

}
