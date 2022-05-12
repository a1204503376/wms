package org.nodes.wms.dao.basics.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.customer.dto.input.CustomerPageQuery;
import org.nodes.wms.dao.basics.customer.dto.output.CustomerSelectResponse;
import org.nodes.wms.dao.basics.customer.dto.output.CustomerResponse;
import org.nodes.wms.dao.basics.customer.entities.BasicsCustomers;

import java.util.List;


/**
 * 客户管理 Mapper 接口
 */
public interface CustomerMapper extends BaseMapper<BasicsCustomers> {
	Page<CustomerResponse> getPage(IPage<?> page, @Param("query") CustomerPageQuery customerPageQuery);

    List<CustomerResponse> getCustomerResponseByQuery(@Param("query") CustomerPageQuery customerPageQuery);

    List<CustomerSelectResponse> listTop10ByCodName(String code, String name);
}
