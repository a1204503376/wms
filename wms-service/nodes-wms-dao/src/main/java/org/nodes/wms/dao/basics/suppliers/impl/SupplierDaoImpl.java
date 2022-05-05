package org.nodes.wms.dao.basics.suppliers.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.suppliers.SupplierDao;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierExportResponse;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.nodes.wms.dao.basics.suppliers.mapper.SupplierMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 供应商 Dao 实现类
 **/
@Service
@RequiredArgsConstructor
public class SupplierDaoImpl extends BaseServiceImpl<SupplierMapper, Supplier> implements SupplierDao {

	private final SupplierMapper supplierMapper;

	@Override
	public Page<SupplierPageResponse> selectPage(IPage<?> page, SupplierPageQuery supplierPagesQuery) {
		return supplierMapper.selectPageSupplier(page, supplierPagesQuery);
	}

	@Override
	public Boolean insert(Supplier supplier) {
		return super.save(supplier);
	}

	@Override
	public Boolean delete(List<Long> ids) {
		return super.deleteLogic(ids);
	}

	@Override
	public boolean isExistSupplierCode(String code) {
		return super.getBaseMapper()
			.selectCount(new LambdaQueryWrapper<Supplier>()
				.eq(Supplier::getCode,code)) > 0;
	}


	@Override
	public List<SupplierExportResponse> selectByConditions(SupplierPageQuery supplierPageQuery) {
		LambdaQueryWrapper<SupplierExportResponse> lambdaQueryWrapper = Wrappers.lambdaQuery();
		lambdaQueryWrapper.like(Func.isNotBlank(supplierPageQuery.getName()),SupplierExportResponse::getName,supplierPageQuery.getName())
			.like(Func.isNotBlank(supplierPageQuery.getSimpleName()),SupplierExportResponse::getSimpleName,supplierPageQuery.getSimpleName())
			.like(Func.isNotBlank(supplierPageQuery.getCode()),SupplierExportResponse::getCode,supplierPageQuery.getCode())
			.ge(Func.isNotEmpty(supplierPageQuery.getCreateTimeBegin()),SupplierExportResponse::getCreateTime,supplierPageQuery.getCreateTimeBegin())
			.le(Func.isNotEmpty(supplierPageQuery.getCreateTimeEnd()),SupplierExportResponse::getCreateTime,supplierPageQuery.getCreateTimeBegin())
			.ge(Func.isNotEmpty(supplierPageQuery.getUpdateTimeBegin()),SupplierExportResponse::getUpdateTime,supplierPageQuery.getUpdateTimeBegin())
			.le(Func.isNotEmpty(supplierPageQuery.getUpdateTimeEnd()),SupplierExportResponse::getUpdateTime,supplierPageQuery.getUpdateTimeEnd());
		return supplierMapper.selectListByWrapper(lambdaQueryWrapper);
	}
}
