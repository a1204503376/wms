package org.nodes.wms.biz.basics.customers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.customer.dto.input.CustomerPageQuery;
import org.nodes.wms.dao.basics.customer.dto.input.CustomerSelectQuery;
import org.nodes.wms.dao.basics.customer.dto.input.NewCustomerRequest;
import org.nodes.wms.dao.basics.customer.dto.input.DeleteCustomerRequest;
import org.nodes.wms.dao.basics.customer.dto.output.CustomerSelectResponse;
import org.nodes.wms.dao.basics.customer.dto.output.CustomerResponse;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 客户管理业务层接口
 */
public interface CustomersBiz {
	/**
	 * 分页查询
    **/
	Page<CustomerResponse> getPage(CustomerPageQuery customerPageQuery, Query query);
	/**
	 *  保存
	 **/
	boolean newCustomers(NewCustomerRequest newCustomerRequest);
	/**
	 *  逻辑删除
	 **/
	boolean remove(DeleteCustomerRequest deleteRequest);
	/**
	 *  导出
	 **/
	void exportExcel(CustomerPageQuery customerPageQuery, HttpServletResponse response);
	/**
	 *  获取客户下拉列别十条数据
	 **/
	List<CustomerSelectResponse> getCustomerSelectResponseTop10List(CustomerSelectQuery customerSelectQuery);
}
