package org.nodes.wms.dao.task.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class SchedulingBroadcastNotificationRequest {
	/**
	 * 任务明细id
	 */
	@NotNull(message = "任务明细id不能为空")
	private String taskDetailId;

	/**
	 * 通知
	 */
	@NotNull(message = "消息不能为空")
	private String msg;
}
