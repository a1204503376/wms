package org.nodes.wms.dao.task.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryAndFrozenEnableOutboundRequest {
	/**
	 * 任务明细
	 */
	@NotNull(message = "任务明细不能为空")
	@ApiModelProperty(value = "任务明细id", required = true)
	private Long taskDetailId;

	/**
	 * 箱型，ABCD
	 */
	@NotNull(message = "箱型不能为空")
	@ApiModelProperty(value = "箱型:ABCD", required = true)
	private String lpnTypeCode;
}
