package org.nodes.wms.core.crontab.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.crontab.entity.Scheme;

/**
 * 任务计划表视图实体类
 *
 * @author NodeX
 * @since 2021-01-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SchemeVO对象", description = "任务计划表")
public class SchemeVO extends Scheme {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;

	/**
	 * 任务名称
	 */
	@ApiModelProperty("任务名称")
	private String taskName;

	/**
	 * 启用状态描述
	 */
	@ApiModelProperty("启用状态描述")
	private String statusDesc;
}
