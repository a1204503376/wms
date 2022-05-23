package org.nodes.wms.dao.instock.receive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.instock.receive.dto.output.DetailReceiveHeaderResponse;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receive.dto.input.ReceivePageQuery;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderResponse;

import java.util.List;

/**
 * 收货管理 Mapper 接口
 */
public interface ReceiveHeaderMapper extends BaseMapper<ReceiveHeader> {
    IPage<ReceiveHeaderResponse> getPage(IPage<ReceiveHeaderResponse> page, @Param("query") ReceivePageQuery receivePageQuery);

	DetailReceiveHeaderResponse selectHeaderById(@Param("id")Long receiveId);

	List<ReceiveHeaderResponse> getReceiveHeaderResponseByQuery(@Param("query") ReceivePageQuery receivePageQuery);

}
