package org.nodes.wms.biz.basics.customers.modular;

import org.nodes.wms.dao.basics.customer.dto.input.newCustomerRequest;
import org.nodes.wms.dao.basics.customer.entities.BasicsCustomers;
import org.springframework.stereotype.Service;
/**
 * 客户管理工厂类
 */
@Service
public class CustomersFactory {
	public BasicsCustomers createCustomers(newCustomerRequest newCustomerRequest) {
		BasicsCustomers basicsCustomers = new BasicsCustomers();
		basicsCustomers.setCode(newCustomerRequest.getCode());
		basicsCustomers.setName(newCustomerRequest.getName());
		basicsCustomers.setSimpleName(newCustomerRequest.getSimpleName());
		basicsCustomers.setStatus(newCustomerRequest.getStatus());
		basicsCustomers.setWoId(newCustomerRequest.getWoId());
		basicsCustomers.setZipCode(newCustomerRequest.getZipCode());
		basicsCustomers.setRemark(newCustomerRequest.getRemark());
		return  basicsCustomers;
	}
}
