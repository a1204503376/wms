package org.nodes.wms.core.stock.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.core.entity.SerialLog;

/**
 * 序列号日志视图实体类
 *
 * @author zx
 * @since 2020-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SerialLogVO对象", description = "序列号日志")
public class SerialLogVO extends SerialLog {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "序列号状态名称")
	private String serialStateName;

	@ApiModelProperty(value = "处理类型名称")
	private String proTypeName;

	@ApiModelProperty(value = "操作人名称")
	private String updateUserName;

	@ApiModelProperty(value = "系统日志内容")
	private String systemProcLog;

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;
}
