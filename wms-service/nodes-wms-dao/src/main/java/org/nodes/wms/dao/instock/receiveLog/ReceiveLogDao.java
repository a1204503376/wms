package org.nodes.wms.dao.instock.receiveLog;

import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;

import java.util.List;

/**
 * 清点记录Dao接口
 */
public interface ReceiveLogDao {
	/**
	 * 根据收货单id获取清点记录
	 * @param receiveId 收货单id
	 */
	List<ReceiveLogResponse> getReceiveLogList(Long receiveId);
}
