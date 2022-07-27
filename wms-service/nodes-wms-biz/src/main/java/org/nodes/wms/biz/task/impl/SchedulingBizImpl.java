package org.nodes.wms.biz.task.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.task.SchedulingBiz;
import org.nodes.wms.biz.task.TaskBiz;
import org.nodes.wms.biz.task.modular.TaskDetailFactory;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.zone.constant.ZoneConstant;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.common.log.dto.input.NoticeMessageRequest;
import org.nodes.wms.dao.task.dto.QueryAndFrozenEnableOutboundRequest;
import org.nodes.wms.dao.task.dto.SchedulingBroadcastNotificationRequest;
import org.nodes.wms.dao.task.dto.SyncTaskStateRequest;
import org.nodes.wms.dao.task.entities.TaskDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulingBizImpl implements SchedulingBiz {
	private final LocationBiz locationBiz;
	private final ZoneBiz zoneBiz;
	private final LogBiz logBiz;
	private final TaskDetailFactory taskDetailFactory;
	private final TaskBiz taskBiz;

	@Override
	public String selectAndFrozenEnableOutbound(QueryAndFrozenEnableOutboundRequest request) {

		// TODO 王
		// 根据箱型（ABC）获取出库接驳区的库位/D箱人工拣货区库位
		// 判断库位是否有库存
		// 如果没有库存则冻结

		return null;
	}

	@Override
	public void broadcastNotificationActivity(List<SchedulingBroadcastNotificationRequest> request) {
		for (SchedulingBroadcastNotificationRequest notificationRequest : request) {
			NoticeMessageRequest message = new NoticeMessageRequest();
			message.setLog(String.format("任务[%s]：[%s]",
					notificationRequest.getTaskDetailId(), notificationRequest.getMsg()));
			logBiz.noticeMesssage(message);
		}
	}

	@Override
	public Boolean synchronizeTaskStatus(SyncTaskStateRequest request) {
		TaskDetail detail = taskDetailFactory.create(request);
		return taskBiz.updateTaskState(detail); // TODO 王 改为抛业务异常的方式
	}
}
