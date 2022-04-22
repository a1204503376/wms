package org.nodes.wms.biz.basics.customers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersPageQuery;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersRequest;
import org.nodes.wms.dao.basics.customers.dto.input.DeleteRequest;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.nodes.wms.dao.basics.customers.entities.BasicsCustomers;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;

/**
 * 客户管理业务层接口
 */
public interface CustomersBiz {
	/**
	 * 分页查询
    **/
	Page<CustomersResponse> page(IPage<CustomersResponse> page, CustomersPageQuery customersPageQuery);
	/**
	 *  保存
	 **/
	int save(CustomersRequest customersRequest);
	/**
	 *  修改
	 **/
	int update(CustomersRequest customersRequest);
	/**
	 *  逻辑删除
	 **/
	int delete(DeleteRequest deleteRequest);
}
