package org.nodes.wms.dao.task.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SyncTaskStateRequest {
	/**
	 * 任务头表id
	 */
	@ApiModelProperty(value = "任务头表id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long taskHeaderId;

	/**
	 * 任务的明细id
	 */
	@NotNull(message = "任务明细ID不能为空")
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "任务的明细id", required = true)
	private Long taskDetailId;

	/**
	 * 任务状态
	 */
	@NotNull(message = "任务状态不能为空")
	@ApiModelProperty(value = "任务状态：5开始执行6执行结束51异常中断", required = true)
	private Integer state;

	/**
	 * 消息
	 */
	@ApiModelProperty(value = "消息")
	private String msg;
}
