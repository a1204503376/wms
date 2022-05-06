package org.nodes.wms.dao.basics.customers.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.customers.CustomerDao;
import org.nodes.wms.dao.basics.customers.dto.input.CustomerPageQuery;
import org.nodes.wms.dao.basics.customers.dto.input.DeleteCustomerRequest;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.nodes.wms.dao.basics.customers.entities.BasicsCustomers;
import org.nodes.wms.dao.basics.customers.mapper.CustomersMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户表 DAO 实现类
 */
@Service
@RequiredArgsConstructor
public class CustomerDaoImpl extends BaseServiceImpl<CustomersMapper,BasicsCustomers> implements CustomerDao {
	 private final CustomersMapper customersMapper;

	@Override
	public boolean insert(BasicsCustomers basicsCustomers) {
		return super.save(basicsCustomers);
	}


	@Override
	public boolean isExistCustomerCode(String code) {
		LambdaQueryWrapper<BasicsCustomers> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(BasicsCustomers::getCode,code);
		int count = super.count(lambdaQueryWrapper);
		return count>0;
	}

	@Override
	public boolean delete(DeleteCustomerRequest deleteRequest) {
		return super.deleteLogic(deleteRequest.getIds());
	}

	@Override
	public List<CustomersResponse> getCustomerResponseByQuery(CustomerPageQuery customerPageQuery) {
		return customersMapper.getCustomerResponseByQuery(customerPageQuery);
	}

	@Override
	public Page<CustomersResponse> selectPage(IPage<CustomersResponse> page, CustomerPageQuery customerPageQuery) {
		return  customersMapper.getPage(page, customerPageQuery);
	}


}
