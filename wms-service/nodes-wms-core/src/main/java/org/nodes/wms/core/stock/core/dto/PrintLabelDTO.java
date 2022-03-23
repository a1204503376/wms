package org.nodes.wms.core.stock.core.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description 补打箱贴DTO
 *
 * @author pengwei
 * @since 2020-07-15
 */
@Data
public class PrintLabelDTO {

	/**
	 * 库存ID
	 */
	@ApiModelProperty("补打箱贴")
	private Long stockId;

	/**
	 * 箱贴类型
	 */
	@ApiModelProperty("箱贴类型")
	private Integer labelType;

	/**
	 * 打印张数
	 */
	@ApiModelProperty("打印张数")
	private Integer printCount;
}
