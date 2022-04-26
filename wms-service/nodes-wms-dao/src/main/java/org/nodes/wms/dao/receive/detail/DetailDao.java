package org.nodes.wms.dao.receive.detail;

import org.nodes.wms.dao.receive.detail.dto.input.DetailRequest;
import org.nodes.wms.dao.receive.detail.dto.output.DetailResponse;
import org.nodes.wms.dao.receive.detail.entities.ReceiveDetail;

import java.util.List;

/**
 * 收货单明细 DAO 接口
 */
public interface DetailDao {
	List<DetailResponse> selectDetailById(Long receiveId);


	List<DetailRequest> selectDetailByHeaderId(Long receiveId);

	boolean delete(List<Long> ids);

	List<Long> selectDetailIdByReceiveId(Long receiveId);
}
