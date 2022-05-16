package org.nodes.wms.dao.instock.receive;

import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailRequest;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailResponse;

import java.util.List;

/**
 * 收货单明细 DAO 接口
 */
public interface ReceiveDetailDao {
	List<ReceiveDetailResponse> selectDetailById(Long receiveId);


	List<ReceiveDetailRequest> selectDetailByHeaderId(Long receiveId);

	boolean delete(List<Long> ids);

	List<Long> selectDetailIdByReceiveId(Long receiveId);
}