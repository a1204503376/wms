package org.nodes.wms.dao.task.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class CancelTaskRequest implements Serializable {

	private static final long serialVersionUID = 2617863679859114028L;

	/**
	 * 任务id
	 */
	@NotNull(message = "任务id不能为空")
	private List<Long> taskIdList;
}
