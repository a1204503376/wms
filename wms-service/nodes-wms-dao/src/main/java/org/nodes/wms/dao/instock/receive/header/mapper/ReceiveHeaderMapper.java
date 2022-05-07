package org.nodes.wms.dao.instock.receive.header.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.instock.receive.header.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receive.header.dto.input.ReceiveHeaderPageQuery;
import org.nodes.wms.dao.instock.receive.header.dto.output.ReceiveHeaderResponse;

/**
 * 收货管理 Mapper 接口
 */
public interface ReceiveHeaderMapper extends BaseMapper<ReceiveHeader> {
    IPage<ReceiveHeaderResponse> getPage(IPage<ReceiveHeaderResponse> page, @Param("query") ReceiveHeaderPageQuery receiveHeaderPageQuery);

	ReceiveHeaderResponse selectHeaderById(@Param("id")Long receiveId);

}
