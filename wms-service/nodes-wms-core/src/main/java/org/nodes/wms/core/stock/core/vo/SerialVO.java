package org.nodes.wms.core.stock.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.core.entity.Serial;

/**
 * 序列号视图实体类
 *
 * @author zx
 * @since 2020-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SerialVO对象", description = "序列号")
public class SerialVO extends Serial {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;

	/**
	 * 序列号状态描述
	 */
	@ApiModelProperty("序列号状态描述")
	private String serialStateDesc;

	/**
	 * 容器编码
	 */
	@ApiModelProperty("容器编码")
	private String lpnCode;



}
