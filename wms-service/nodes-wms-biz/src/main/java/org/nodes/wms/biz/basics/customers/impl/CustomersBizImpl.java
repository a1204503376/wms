package org.nodes.wms.biz.basics.customers.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.customers.CustomersBiz;
import org.nodes.wms.biz.basics.customers.modular.CustomersFactory;
import org.nodes.wms.dao.basics.customers.CustomersDao;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersPageQuery;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersRequest;
import org.nodes.wms.dao.basics.customers.dto.input.DeleteCustomersRequest;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.nodes.wms.dao.basics.customers.entities.BasicsCustomers;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;

/**
 * 客户管理 业务类
 */
@Service
@RequiredArgsConstructor
public class CustomersBizImpl implements CustomersBiz {
	private  final CustomersDao customersDao;
	private  final CustomersFactory customersFactory;

	@Override
	public Page<CustomersResponse> getPage(Query query, CustomersPageQuery customersPageQuery) {
		IPage<CustomersResponse> page = Condition.getPage(query);
		return customersDao.selectPage(page,customersPageQuery);
	}

	@Override
	public boolean saveCustomers(CustomersRequest customersRequest) {
		BasicsCustomers basicsCustomers = customersFactory.createCustomers(customersRequest);
		boolean hasCode =  customersDao.findByCode(basicsCustomers.getCode());
		if(hasCode){
			throw new ServiceException("客户编码重复");
		}

		return  customersDao.insert(basicsCustomers);
	}

	@Override
	public boolean remove(DeleteCustomersRequest deleteRequest) {
		return customersDao.delete(deleteRequest);
	}
}
