package org.nodes.wms.dao.task.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryAndFrozenEnableOutboundRequest {
	/**
	 * 任务明细
	 */
	@NotNull(message = "任务明细不能为空")
	private Long taskDetailId;

	/**
	 * 箱型，ABCD
	 */
	@NotNull(message = "箱型不能为空")
	private String lpnTypeCode;
}
