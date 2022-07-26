package org.nodes.wms.biz.task;

import org.nodes.wms.dao.task.dto.QueryAndFrozenEnableOutboundRequest;
import org.nodes.wms.dao.task.dto.SchedulingBroadcastNotificationRequest;
import org.nodes.wms.dao.task.dto.SyncTaskStateRequest;

import java.util.List;

/**
 * 天宜定制，调度系统业务层
 * @author T
 */
public interface SchedulingBiz {
	/**
	 * 查询可用的出库接驳区库位，并冻结
	 *
	 * @param request 包含任务ID和箱型
	 * @return 合适的库位编码
	 */
	String selectAndFrozenEnableOutbound(QueryAndFrozenEnableOutboundRequest request);

	/**
	 * 广播日志 调用日志广播通知渠道进行消息存储
	 *
	 * @param request 里面包含日志消息和任务ID
	 */
	void broadcastNotificationActivity(List<SchedulingBroadcastNotificationRequest> request);

	/**
	 * 同步状态
	 *
	 * @param request 请求参数
	 */
	Boolean synchronizeTaskStatus(SyncTaskStateRequest request);
}
