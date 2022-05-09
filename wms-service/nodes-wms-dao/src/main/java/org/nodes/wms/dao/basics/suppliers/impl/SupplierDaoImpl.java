package org.nodes.wms.dao.basics.suppliers.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
	public List<SupplierExportResponse> listBySupplierPageQuery(SupplierPageQuery supplierPageQuery) {
		QueryWrapper<SupplierExportResponse> queryWrapper = Wrappers.query();
		queryWrapper
			.like(Func.isNotBlank(supplierPageQuery.getName()),"s.name",supplierPageQuery.getName())
			.like(Func.isNotBlank(supplierPageQuery.getSimpleName()),"s.simpleName",supplierPageQuery.getSimpleName())
			.like(Func.isNotBlank(supplierPageQuery.getCode()),"s.code",supplierPageQuery.getCode())
			.ge(Func.isNotEmpty(supplierPageQuery.getCreateTimeBegin()),"s.create_time",supplierPageQuery.getCreateTimeBegin())
			.le(Func.isNotEmpty(supplierPageQuery.getCreateTimeEnd()),"s.create_time",supplierPageQuery.getCreateTimeBegin())
			.ge(Func.isNotEmpty(supplierPageQuery.getUpdateTimeBegin()),"s.update_time",supplierPageQuery.getUpdateTimeBegin())
			.le(Func.isNotEmpty(supplierPageQuery.getUpdateTimeEnd()),"s.update_time",supplierPageQuery.getUpdateTimeEnd());
		return super.baseMapper.selectListByWrapper(queryWrapper);
	}
}
