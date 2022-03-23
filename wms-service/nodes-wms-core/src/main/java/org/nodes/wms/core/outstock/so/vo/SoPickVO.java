package org.nodes.wms.core.outstock.so.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.outstock.so.entity.SoPick;

/**
 * 拣货记录日志视图实体类
 *
 * @author zx
 * @since 2020-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SoPickVO对象", description = "拣货记录日志")
public class SoPickVO extends SoPick {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "库房名称")
	private String whName;

	@ApiModelProperty(value = "包装名称")
	private String wspName;

	@ApiModelProperty(value = "层级名称")
	private String skuLevelName;

	/**
	 * 拼托状态
	 */
	@ApiModelProperty("拼托状态")
	private String fillLpnStateDesc;
}
