package org.nodes.wms.biz.instock.receiveLog;

import org.nodes.wms.dao.common.log.dto.output.LogReceiveResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;

import java.util.List;

/**
 * 清点记录业务层接口
 */
public interface ReceiveLogBiz {
	/**
	 * 根据收货单id获取清点记录集合
	 * @param receiveId 收货单id
	 */
	List<ReceiveLogResponse> getReceiveLogList(Long receiveId);

}
