package org.nodes.wms.dao.basics.customers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersPageQuery;
import org.nodes.wms.dao.basics.customers.dto.input.DeleteCustomersRequest;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.nodes.wms.dao.basics.customers.entities.BasicsCustomers;

/**
 * 客户表 DAO 接口
 */
public interface CustomersDao  {

	/**
	 * 分页查询
	 *
	 * @param page            分页对象
	 * @param customersPageQuery 分页请求参数
	 * @return IPage<PageResponse>
	 */
	Page<CustomersResponse> selectPage(IPage<CustomersResponse> page, CustomersPageQuery customersPageQuery);

	boolean  insert(BasicsCustomers basicsCustomers);


	boolean findByCode(String code);

	boolean delete(DeleteCustomersRequest deleteRequest);



}
