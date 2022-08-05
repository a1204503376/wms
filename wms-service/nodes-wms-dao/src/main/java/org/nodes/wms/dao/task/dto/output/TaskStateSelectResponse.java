package org.nodes.wms.dao.task.dto.output;

import lombok.Data;
import org.nodes.wms.dao.application.dto.output.ElementUiSelectResponse;

/**
 * 任务状态下拉框返回前端dto
 */
@Data
public class TaskStateSelectResponse extends ElementUiSelectResponse<Integer> {
}
