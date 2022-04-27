package org.nodes.wms.dao.receive.header.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.nodes.wms.dao.basics.customers.entities.BasicsCustomers;
import org.nodes.wms.dao.receive.detail.dto.output.DetailResponse;
import org.nodes.wms.dao.receive.header.dto.input.HeaderPageQuery;
import org.nodes.wms.dao.receive.header.dto.output.HeaderResponse;
import org.nodes.wms.dao.receive.header.entities.ReceiveHeader;

import java.util.List;

/**
 * 收货管理 Mapper 接口
 */
public interface HeaderMapper extends BaseMapper<ReceiveHeader> {
    IPage<HeaderResponse> getPage(IPage<HeaderResponse> page, @Param("query") HeaderPageQuery headerPageQuery);

	HeaderResponse selectHeaderById(@Param("id")Long receiveId);

}
