package org.nodes.wms.dao.basics.customer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.customer.dto.input.CustomerPageQuery;
import org.nodes.wms.dao.basics.customer.dto.input.DeleteCustomerRequest;
import org.nodes.wms.dao.basics.customer.dto.output.CustomerSelectResponse;
import org.nodes.wms.dao.basics.customer.dto.output.CustomerResponse;
import org.nodes.wms.dao.basics.customer.entities.BasicsCustomers;

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
	Page<CustomerResponse> selectPage(IPage<CustomerResponse> page, CustomerPageQuery customerPageQuery);

	boolean  insert(BasicsCustomers basicsCustomers);


	boolean isExistCustomerCode(String code);

	boolean delete(DeleteCustomerRequest deleteRequest);


    List<CustomerResponse> getCustomerResponseByQuery(CustomerPageQuery customerPageQuery);

    List<CustomerSelectResponse> listTop10ByCodeName(String code, String name);
}
