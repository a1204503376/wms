package org.nodes.wms.core.crontab.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.crontab.entity.CrontabTask;

/**
 * 任务表数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-01-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CrontabTaskDTO extends CrontabTask {
	private static final long serialVersionUID = 1L;

}
