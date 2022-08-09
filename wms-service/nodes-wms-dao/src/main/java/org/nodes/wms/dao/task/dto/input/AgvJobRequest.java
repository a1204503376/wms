package org.nodes.wms.dao.task.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 继续、取消、停止AGV任务
 **/
@Data
public class AgvJobRequest implements Serializable {

	private static final long serialVersionUID = 7676175512966558447L;

	/**
	 * 任务id
	 */
	private Long wmsTaskId;

	/**
	 * 任务明细id
	 */
	private Long wmsTaskDetailId;
}
