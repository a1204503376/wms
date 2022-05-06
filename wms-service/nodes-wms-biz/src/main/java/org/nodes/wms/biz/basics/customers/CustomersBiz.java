package org.nodes.wms.biz.basics.customers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.customers.dto.input.CustomerPageQuery;
import org.nodes.wms.dao.basics.customers.dto.input.newCustomerRequest;
import org.nodes.wms.dao.basics.customers.dto.input.DeleteCustomerRequest;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;

/**
 * 客户管理业务层接口
 */
public interface CustomersBiz {
	/**
	 * 分页查询
    **/
	Page<CustomersResponse> getPage(CustomerPageQuery customerPageQuery,Query query);
	/**
	 *  保存
	 **/
	boolean newCustomers(newCustomerRequest newCustomerRequest);
	/**
	 *  逻辑删除
	 **/
	boolean remove(DeleteCustomerRequest deleteRequest);

	void exportExcel(CustomerPageQuery customerPageQuery, HttpServletResponse response);

}
