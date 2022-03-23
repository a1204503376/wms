
package org.nodes.wms.core.stock.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.core.entity.LotLog;

/**
 * 批次号日志视图实体类
 *
 * @author pengwei
 * @since 2020-03-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LotLogVO对象", description = "批次号日志")
public class LotLogVO extends LotLog {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;

	/**
	 * 处理类型描述
	 */
	@ApiModelProperty("处理类型描述")
	private String proTypeDesc;


	@ApiModelProperty(value = "操作人名称")
	private String updateUserName;

	@ApiModelProperty(value = "系统日志内容")
	private String systemProcLog;
}
