package org.nodes.wms.biz.basics.customers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersPageQuery;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersRequest;
import org.nodes.wms.dao.basics.customers.dto.input.DeleteCustomersRequest;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.springblade.core.mp.support.Query;

/**
 * 客户管理业务层接口
 */
public interface CustomersBiz {
	/**
	 * 分页查询
    **/
	Page<CustomersResponse> getPage(Query query, CustomersPageQuery customersPageQuery);
	/**
	 *  保存
	 **/
	boolean saveCustomers(CustomersRequest customersRequest);
	/**
	 *  逻辑删除
	 **/
	boolean remove(DeleteCustomersRequest deleteRequest);
}
