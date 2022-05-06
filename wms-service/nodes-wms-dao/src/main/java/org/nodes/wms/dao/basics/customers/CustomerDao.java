package org.nodes.wms.dao.basics.customers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.customers.dto.input.CustomerPageQuery;
import org.nodes.wms.dao.basics.customers.dto.input.DeleteCustomerRequest;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.nodes.wms.dao.basics.customers.entities.BasicsCustomers;

import java.util.List;

/**
 * 客户表 DAO 接口
 */
public interface CustomerDao {

	/**
	 * 分页查询
	 *
	 * @param page            分页对象
	 * @param customerPageQuery 分页请求参数
	 * @return IPage<PageResponse>
	 */
	Page<CustomersResponse> selectPage(IPage<CustomersResponse> page, CustomerPageQuery customerPageQuery);

	boolean  insert(BasicsCustomers basicsCustomers);


	boolean isExistCustomerCode(String code);

	boolean delete(DeleteCustomerRequest deleteRequest);


    List<CustomersResponse> getCustomerResponseByQuery(CustomerPageQuery customerPageQuery);
}
