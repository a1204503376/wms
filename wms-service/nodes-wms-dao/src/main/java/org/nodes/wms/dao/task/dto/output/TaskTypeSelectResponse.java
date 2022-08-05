package org.nodes.wms.dao.task.dto.output;

import lombok.Data;
import org.nodes.wms.dao.application.dto.output.ElementUiSelectResponse;

/**
 * 任务类型下拉组件返回前端dto
 */
@Data
public class TaskTypeSelectResponse extends ElementUiSelectResponse<Integer> {
}
