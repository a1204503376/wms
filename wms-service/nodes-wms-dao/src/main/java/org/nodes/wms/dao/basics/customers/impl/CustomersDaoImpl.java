package org.nodes.wms.dao.basics.customers.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.customers.CustomersDao;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersPageQuery;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersRequest;
import org.nodes.wms.dao.basics.customers.dto.input.DeleteRequest;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.nodes.wms.dao.basics.customers.entities.BasicsCustomers;
import org.nodes.wms.dao.basics.customers.mapper.CustomersMapper;
import org.nodes.wms.dao.instock.asn.mapper.AsnHeaderMapper;
import org.springframework.stereotype.Service;

/**
 * 客户表 DAO 实现类
 */
@Service
@RequiredArgsConstructor
public class CustomersDaoImpl implements CustomersDao {
	 private final CustomersMapper customersMapper;
	@Override
	public Page<CustomersResponse> getCustomersPage(IPage<?> page, CustomersPageQuery customersPageQuery) {

		return customersMapper.getCustomersPage(page, customersPageQuery);
	}

	@Override
	public int save(BasicsCustomers basicsCustomers) {
		return customersMapper.insert(basicsCustomers);
	}

	@Override
	public int update(BasicsCustomers basicsCustomers) {
		return customersMapper.updateById(basicsCustomers);
	}

	@Override
	public String findByCode(String code) {
		return customersMapper.selectByCode(code);
	}

	@Override
	public int delete(DeleteRequest deleteRequest) {
		return customersMapper.updateByIds(deleteRequest);
	}


}
