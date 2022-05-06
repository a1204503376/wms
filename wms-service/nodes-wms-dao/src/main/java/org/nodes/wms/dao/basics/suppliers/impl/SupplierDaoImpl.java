package org.nodes.wms.dao.basics.suppliers.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.suppliers.SupplierDao;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.nodes.wms.dao.basics.suppliers.mapper.SupplierMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 供应商 Dao 实现类
 **/
@Repository
@RequiredArgsConstructor
public class SupplierDaoImpl extends BaseServiceImpl<SupplierMapper, Supplier> implements SupplierDao {

	@Override
	public Page<SupplierPageResponse> selectPage(IPage<?> page, SupplierPageQuery supplierPagesQuery) {
		return super.baseMapper.selectPageSupplier(page, supplierPagesQuery);
	}

	@Override
	public boolean insert(Supplier supplier) {
		return super.save(supplier);
	}

	@Override
	public boolean delete(List<Long> ids) {
		return super.deleteLogic(ids);
	}

	@Override
	public boolean isExistSupplierCode(String code) {
		return super.getBaseMapper()
			.selectCount(new LambdaQueryWrapper<Supplier>()
				.eq(Supplier::getCode,code)) > 0;
	}


	@Override
	public List<Supplier> listBySupplierPageQuery(SupplierPageQuery supplierPageQuery) {
		LambdaQueryWrapper<Supplier> queryWrapper = Wrappers.lambdaQuery(Supplier.class);
		queryWrapper.like(Func.isNotBlank(supplierPageQuery.getName()),Supplier::getName,supplierPageQuery.getName())
			.like(Func.isNotBlank(supplierPageQuery.getSimpleName()),Supplier::getSimpleName,supplierPageQuery.getSimpleName())
			.like(Func.isNotBlank(supplierPageQuery.getCode()),Supplier::getCode,supplierPageQuery.getCode())
			.ge(Func.isNotEmpty(supplierPageQuery.getCreateTimeBegin()),Supplier::getCreateTime,supplierPageQuery.getCreateTimeBegin())
			.le(Func.isNotEmpty(supplierPageQuery.getCreateTimeEnd()),Supplier::getCreateTime,supplierPageQuery.getCreateTimeBegin())
			.ge(Func.isNotEmpty(supplierPageQuery.getUpdateTimeBegin()),Supplier::getUpdateTime,supplierPageQuery.getUpdateTimeBegin())
			.le(Func.isNotEmpty(supplierPageQuery.getUpdateTimeEnd()),Supplier::getUpdateTime,supplierPageQuery.getUpdateTimeEnd());
		return super.list(queryWrapper);
	}
}
