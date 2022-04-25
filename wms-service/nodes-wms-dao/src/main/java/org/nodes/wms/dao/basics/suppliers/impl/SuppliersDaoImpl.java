package org.nodes.wms.dao.basics.suppliers.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.suppliers.SuppliersDao;
import org.nodes.wms.dao.basics.suppliers.dto.input.SuppliersPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.output.SuppliersPageResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Suppliers;
import org.nodes.wms.dao.basics.suppliers.mapper.SuppliersMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 供应商 Dao 实现类
 **/
@Service
@RequiredArgsConstructor
public class SuppliersDaoImpl extends BaseServiceImpl<SuppliersMapper,Suppliers> implements SuppliersDao {

	private final SuppliersMapper suppliersMapper;

	@Override
	public Page<SuppliersPageResponse> selectPage(IPage<?> page, SuppliersPageQuery suppliersPagesQuery) {
		return suppliersMapper.selectPageSuppliers(page, suppliersPagesQuery);
	}

	@Override
	public Boolean addSuppliers(Suppliers suppliers) {
		return super.save(suppliers);
	}

	@Override
	public Boolean deleteSuppliersByIds(List<Long> ids) {
		return super.deleteLogic(ids);
	}

	@Override
	public Integer selectCountSupplierCode(String code) {
		return super.getBaseMapper().selectCount(new LambdaQueryWrapper<Suppliers>().eq(Suppliers::getCode,code));
	}
}
