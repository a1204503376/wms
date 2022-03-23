package org.nodes.wms.core.stock.core.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StockFrozenDTO {
	/**
	 * 冻结类型
	 * singleFrozen:单个冻结,multFrozen:批量冻结,locFrozen:库位冻结
	 */
	@ApiModelProperty("冻结类型")
	@NotBlank(message = "冻结类型不能为空！")
	private String type;

	/**
	 * 冻结状态
	 * 1:冻结  0:解冻
	 */
	@ApiModelProperty("冻结状态")
	private String flag;

	/**
	 * 单个库存id|多个库存id|库位编号
	 */
	@ApiModelProperty("查询内容")
	@NotBlank(message = "查询内容不能为空！")
	private String queryKey;
}
