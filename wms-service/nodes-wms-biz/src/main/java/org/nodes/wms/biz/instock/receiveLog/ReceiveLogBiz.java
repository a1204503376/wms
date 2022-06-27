package org.nodes.wms.biz.instock.receiveLog;

import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogIndexResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;

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

	/**
	 * 查找7天内入库量前10的物品
	 *
	 * @return List<ReceiveLogIndexResponse>
	 */
    List<ReceiveLogIndexResponse> findReceiveSkuQtyTop10();

	/**
	 * 创建清点记录
	 * @param receiveLog 外部只需要赋值业务参数
	 * @return
	 */
	ReceiveLog newReceiveLog(ReceiveLog receiveLog);
}
