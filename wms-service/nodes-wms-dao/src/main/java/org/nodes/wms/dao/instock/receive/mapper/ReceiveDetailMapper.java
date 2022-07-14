package org.nodes.wms.dao.instock.receive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveByPcQuery;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailPdaQuery;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailRequest;
import org.nodes.wms.dao.instock.receive.dto.output.DetailReceiveDetailPdaResponse;
import org.nodes.wms.dao.instock.receive.dto.output.DetailReceiveDetailResponse;
import org.nodes.wms.dao.instock.receive.dto.output.EditReceiveDetailResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailByPcResponse;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetail;

import java.util.List;

/**
 * 收货单明细 Mapper 接口
 */
public interface ReceiveDetailMapper extends BaseMapper<ReceiveDetail> {
	List<DetailReceiveDetailResponse> selectDetailById(@Param("id") Long receiveId);

	List<ReceiveDetailRequest> selectDetailByHeaderId(@Param("id") Long receiveId);

	List<Long> selectDetailIdByReceiveId(@Param("id") Long receiveId);

	List<EditReceiveDetailResponse> selectReceiveDetailById(@Param("id") Long receiveId);

	/**
	 * @param receiveDetailPdaQuery 收货单接收前端请求条件
	 * @return 收货单明细表集合
	 */
	List<DetailReceiveDetailPdaResponse> selectDetailListByReceiveId(@Param("query") ReceiveDetailPdaQuery receiveDetailPdaQuery);

	String selectReceiveDetailLinNo(Long receiveId);

	/**
	 * pc收货查询返回前端明细对象
	 */
	ReceiveDetailByPcResponse selectReceiveDetailByPcResponse(@Param("query") ReceiveByPcQuery receiveByPcQuery);
}
