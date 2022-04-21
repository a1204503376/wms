package org.nodes.wms.dao.basics.customers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersPageQuery;
import org.nodes.wms.dao.basics.customers.dto.input.DeleteRequest;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.nodes.wms.dao.basics.customers.entities.BasicsCustomers;


/**
 * 客户管理 Mapper 接口
 */
public interface CustomersMapper extends BaseMapper<BasicsCustomers> {
	Page<CustomersResponse> getCustomersPage(IPage<?> page,@Param("query") CustomersPageQuery customersPageQuery);

    String selectByCode(@Param("code")String code);

	int updateByIds(@Param("deleteRequest") DeleteRequest deleteRequest);
}
