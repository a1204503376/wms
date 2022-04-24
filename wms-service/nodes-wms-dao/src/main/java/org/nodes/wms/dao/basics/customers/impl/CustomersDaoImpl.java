package org.nodes.wms.dao.basics.customers.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.customers.CustomersDao;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersPageQuery;
import org.nodes.wms.dao.basics.customers.dto.input.DeleteCustomersRequest;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.nodes.wms.dao.basics.customers.entities.BasicsCustomers;
import org.nodes.wms.dao.basics.customers.mapper.CustomersMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 客户表 DAO 实现类
 */
@Service
@RequiredArgsConstructor
public class CustomersDaoImpl extends BaseServiceImpl<CustomersMapper,BasicsCustomers> implements CustomersDao {
	 private final CustomersMapper customersMapper;

	@Override
	public boolean insert(BasicsCustomers basicsCustomers) {
		return super.save(basicsCustomers);
	}


	@Override
	public boolean findByCode(String code) {
		LambdaQueryWrapper<BasicsCustomers> lambdaQueryWrapper = new LambdaQueryWrapper();
		lambdaQueryWrapper.eq(BasicsCustomers::getCode,code);
		int count = super.count(lambdaQueryWrapper);
		return count>0;
	}

	@Override
	public boolean delete(DeleteCustomersRequest deleteRequest) {
		return super.deleteLogic(deleteRequest.getList());
	}

	@Override
	public Page<CustomersResponse> selectPage(IPage<CustomersResponse> page, CustomersPageQuery customersPageQuery) {
		return  customersMapper.getPage(page, customersPageQuery);
	}


}
