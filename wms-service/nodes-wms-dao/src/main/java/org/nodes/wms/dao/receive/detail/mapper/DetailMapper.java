package org.nodes.wms.dao.receive.detail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.receive.detail.dto.input.DetailRequest;
import org.nodes.wms.dao.receive.detail.dto.output.DetailResponse;
import org.nodes.wms.dao.receive.detail.entities.ReceiveDetail;
import org.nodes.wms.dao.receive.header.entities.ReceiveHeader;

import java.math.BigDecimal;
import java.util.List;

/**
 * 收货单明细 Mapper 接口
 */
public interface DetailMapper  extends BaseMapper<ReceiveDetail> {
	List<DetailResponse> selectDetailById(@Param("id")Long receiveId);

	List<DetailRequest> selectDetailByHeaderId(@Param("id")Long receiveId);

	List<Long> selectDetailIdByReceiveId(@Param("id")Long receiveId);
}
