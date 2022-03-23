
package org.nodes.wms.core.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.system.entity.TaskHistory;

/**
 * 任务履历视图实体类
 *
 * @author pengwei
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TaskHistoryVO对象", description = "任务履历")
public class TaskHistoryVO extends TaskHistory {
	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;
	/**
	 * 任务类型
	 */
	@ApiModelProperty("任务类型")
	private String taskTypeName;
	/**
	 * 任务执行方式
	 */
	@ApiModelProperty("任务执行方式")
	private String taskProcTypeName;
	/**
	 * 单据种类
	 */
	@ApiModelProperty("单据种类")
	private String billTypeName;
	/**
	 * 工作区
	 */
	@ApiModelProperty("工作区")
	private String wwaName;

}
