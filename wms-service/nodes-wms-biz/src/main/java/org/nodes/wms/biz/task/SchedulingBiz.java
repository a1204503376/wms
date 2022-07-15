package org.nodes.wms.biz.task;

import org.nodes.wms.dao.task.dto.QueryAndFrozenEnableOutboundRequest;

public interface SchedulingBiz {
	/**
	 * 查询可用的出库接驳区库位，并冻结
	 *
	 * @param request 包含任务ID和箱型
	 * @return 合适的库位编码
	 */
	String selectAndFrozenEnableOutbound(QueryAndFrozenEnableOutboundRequest request);
}
