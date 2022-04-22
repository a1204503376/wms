package org.nodes.wms.biz.basics.customers.modular;

import org.nodes.wms.dao.basics.customers.dto.input.CustomersRequest;
import org.nodes.wms.dao.basics.customers.entities.BasicsCustomers;
import org.springframework.stereotype.Service;
/**
 * 客户管理工厂类
 */
@Service
public class CustomersFactory {
	public BasicsCustomers createCustomers(CustomersRequest customersRequest) {
		BasicsCustomers basicsCustomers = new BasicsCustomers();
		basicsCustomers.setId(customersRequest.getId());
		basicsCustomers.setCode(customersRequest.getCode());
		basicsCustomers.setName(customersRequest.getName());
		basicsCustomers.setSimpleName(customersRequest.getSimpleName());
		basicsCustomers.setStatus(customersRequest.getStatus());
		basicsCustomers.setWoId(customersRequest.getWoId());
		basicsCustomers.setZipCode(customersRequest.getZipCode());
		basicsCustomers.setRemark(customersRequest.getRemark());
		return  basicsCustomers;
	}
}
