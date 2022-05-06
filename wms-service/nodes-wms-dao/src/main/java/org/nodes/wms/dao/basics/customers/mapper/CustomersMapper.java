package org.nodes.wms.dao.basics.customers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.customers.dto.input.CustomerPageQuery;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.nodes.wms.dao.basics.customers.entities.BasicsCustomers;

import java.util.List;


/**
 * 客户管理 Mapper 接口
 */
public interface CustomersMapper extends BaseMapper<BasicsCustomers> {
	Page<CustomersResponse> getPage(IPage<?> page,@Param("query") CustomerPageQuery customerPageQuery);

    List<CustomersResponse> getCustomerResponseByQuery(@Param("query") CustomerPageQuery customerPageQuery);
}
