package org.nodes.wms.biz.task;

import org.nodes.wms.dao.task.dto.QueryAndFrozenEnableOutboundRequest;
import org.nodes.wms.dao.task.dto.SchedulingBroadcastNotificationRequest;
import org.nodes.wms.dao.task.dto.SyncTaskStateRequest;
import org.nodes.wms.dao.task.dto.input.NewLocationOnDoubleWarehousingRequest;

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
	void synchronizeTaskStatus(SyncTaskStateRequest request);

	/**
	 * 双重入库推荐新的库位。
	 * 1. 原来的目标库位使用状态有系统业务冻结改为冻结，并清空loc_flag_desc
	 * 2. 生成新的目标库位，并冻结目标库位
	 * 3. 更新任务中的目标库位和消息
	 * 4. 发送通知消息
	 *
	 * @param request
	 * @return 新的库位编码
	 */
	String newLocationOnDoubleWarehousing(NewLocationOnDoubleWarehousingRequest request);
}
