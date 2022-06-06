package org.nodes.wms.biz.basics.customers.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.customers.CustomersBiz;
import org.nodes.wms.biz.basics.customers.modular.CustomersFactory;
import org.nodes.wms.dao.basics.customer.CustomerDao;
import org.nodes.wms.dao.basics.customer.dto.input.*;
import org.nodes.wms.dao.basics.customer.dto.output.CustomerResponse;
import org.nodes.wms.dao.basics.customer.dto.output.CustomerSelectResponse;
import org.nodes.wms.dao.basics.customer.entities.BasicsCustomer;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 客户管理 业务类
 */
@Service
@RequiredArgsConstructor
public class CustomersBizImpl implements CustomersBiz {
	private  final CustomerDao customerDao;
	private  final CustomersFactory customersFactory;

	@Override
	public Page<CustomerResponse> getPage(CustomerPageQuery customerPageQuery, Query query) {
		IPage<CustomerResponse> page = Condition.getPage(query);
		return customerDao.selectPage(page, customerPageQuery);
	}

	@Override
	public boolean newCustomers(NewCustomerRequest newCustomerRequest) {
		boolean isExist = customerDao.isExistCustomerCode(newCustomerRequest.getCode());
		if(isExist){
			throw new ServiceException("新增客户失败，客户编码重复");
		}
		BasicsCustomer basicsCustomer = customersFactory.createCustomers(newCustomerRequest);
		return  customerDao.insert(basicsCustomer);
	}

	@Override
	public boolean remove(DeleteCustomerRequest deleteRequest) {
		return customerDao.delete(deleteRequest);
	}

	@Override
	public void exportExcel(CustomerPageQuery customerPageQuery, HttpServletResponse response) {
		List<CustomerResponse> customerResponseList = customerDao.getCustomerResponseByQuery(customerPageQuery);
		ExcelUtil.export(response, "客户", "客户数据表", customerResponseList, CustomerResponse.class);
	}

	@Override
	public List<CustomerSelectResponse> getCustomerSelectResponseTop10List(CustomerSelectQuery customerSelectQuery) {
		return customerDao.listTop10ByCodeName(customerSelectQuery.getKey(), customerSelectQuery.getKey());
	}

	@Override
	public boolean importExcel(List<CustomerImportRequest> importDataList) {
		if(Func.isEmpty(importDataList)){
			throw new ServiceException("导入失败，没有可导入的数据");
		}
		List<BasicsCustomer> customerList = customersFactory.createCustomerListForImport(importDataList);
		return customerDao.importExcel(customerList);
	}

	@Override
	public BasicsCustomer findCustomerById(Long id) {
		return customerDao.getCustomerById(id);

	}

	@Override
	public BasicsCustomer findCustomerByCode(String code) {
		return customerDao.getCustomerByCode(code);
	}
}
