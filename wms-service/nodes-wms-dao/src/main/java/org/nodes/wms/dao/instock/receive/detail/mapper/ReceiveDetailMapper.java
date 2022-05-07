package org.nodes.wms.dao.instock.receive.detail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.instock.receive.detail.entities.ReceiveDetail;
import org.nodes.wms.dao.instock.receive.detail.dto.input.ReceiveDetailRequest;
import org.nodes.wms.dao.instock.receive.detail.dto.output.ReceiveDetailResponse;

import java.util.List;

/**
 * 收货单明细 Mapper 接口
 */
public interface ReceiveDetailMapper extends BaseMapper<ReceiveDetail> {
	List<ReceiveDetailResponse> selectDetailById(@Param("id")Long receiveId);

	List<ReceiveDetailRequest> selectDetailByHeaderId(@Param("id")Long receiveId);

	List<Long> selectDetailIdByReceiveId(@Param("id")Long receiveId);
}
