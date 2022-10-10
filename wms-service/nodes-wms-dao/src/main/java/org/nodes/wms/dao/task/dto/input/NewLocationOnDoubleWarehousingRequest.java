package org.nodes.wms.dao.task.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewLocationOnDoubleWarehousingRequest {

	@NotNull(message = "任务id不能为空")
	private String taskId;
}
