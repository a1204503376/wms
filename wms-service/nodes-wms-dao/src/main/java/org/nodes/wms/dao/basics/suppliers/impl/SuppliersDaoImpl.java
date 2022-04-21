package org.nodes.wms.dao.basics.suppliers.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.suppliers.SuppliersDao;
import org.nodes.wms.dao.basics.suppliers.dto.input.SuppliersPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.output.SuppliersPageResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Suppliers;
import org.nodes.wms.dao.basics.suppliers.mapper.SuppliersMapper;
import org.springframework.stereotype.Service;

/**
 * 供应商 Dao 实现类
 **/
@Service
@RequiredArgsConstructor
public class SuppliersDaoImpl implements SuppliersDao {

	private final SuppliersMapper suppliersMapper;

	@Override
	public Page<SuppliersPageResponse> queryPageSuppliers(IPage<?> page, SuppliersPageQuery suppliersPagesQuery) {
		return suppliersMapper.selectPageSuppliers(page, suppliersPagesQuery);
	}

	@Override
	public Integer addSuppliers(Suppliers suppliers) {
		return suppliersMapper.insert(suppliers);
	}

	@Override
	public Integer deleteSuppliersById(Long[] ids) {
		return suppliersMapper.logicDeleteByIds(ids);
	}

	@Override
	public Integer updateSuppliers(Suppliers suppliers) {
		return suppliersMapper.update(suppliers,new QueryWrapper<Suppliers>().eq("id",suppliers.getId()));
	}

	@Override
	public Integer findSupplierCodeExist(String code) {
		return suppliersMapper.selectCount(new LambdaQueryWrapper<Suppliers>().eq(Suppliers::getCode,code));
	}
}
