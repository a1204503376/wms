package org.nodes.wms.core.crontab.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.crontab.entity.CrontabTask;

/**
 * 任务表视图实体类
 *
 * @author NodeX
 * @since 2021-01-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TaskVO对象", description = "任务表")
public class CrontabTaskVO extends CrontabTask {
	private static final long serialVersionUID = 1L;

	private String httpMethodDesc;
}
