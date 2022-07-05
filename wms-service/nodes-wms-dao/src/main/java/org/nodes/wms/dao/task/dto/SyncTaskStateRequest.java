package org.nodes.wms.dao.task.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SyncTaskStateRequest {
	/**
	 * 任务头表id
	 */
	private Long taskHeaderId;

	/**
	 * 任务的明细id
	 */
	@NotNull(message = "任务明细ID不能为空")
	private Long taskDetailId;

	/**
	 * 任务状态
	 */
	@NotNull(message = "任务状态不能为空")
	private Integer state;

}
