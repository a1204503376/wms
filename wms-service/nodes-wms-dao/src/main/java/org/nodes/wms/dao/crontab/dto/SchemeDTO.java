package org.nodes.wms.dao.crontab.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.crontab.entity.Scheme;

/**
 * 任务计划表数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-01-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SchemeDTO extends Scheme {
	private static final long serialVersionUID = 1L;

}
