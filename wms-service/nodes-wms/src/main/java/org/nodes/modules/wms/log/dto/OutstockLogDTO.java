package org.nodes.modules.wms.log.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.strategy.entity.OutstockLog;

/**
 * 分配记录数据传输对象实体类
 *
 * @author pengwei
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OutstockLogDTO extends OutstockLog {
	private static final long serialVersionUID = 1L;

	/**
	 * 执行时间范围（JSON 字符串）
	 */
	@ApiModelProperty("执行时间范围（JSON 字符串）")
	private String solTimeRange;
}
