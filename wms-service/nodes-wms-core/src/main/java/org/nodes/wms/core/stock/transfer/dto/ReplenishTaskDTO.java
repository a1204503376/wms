package org.nodes.wms.core.stock.transfer.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import java.math.BigDecimal;

/**
 * 补货任务DTO
 *
 * @author pengwei
 * @since 2020-08-03
 */
@Data
public class ReplenishTaskDTO {

	/**
	 * 涉及的出库单ID集合
	 */
	@ApiModelProperty("涉及的出库单ID集合")
	String soBillIdsStr;

	/**
	 * 库房ID
	 */
	@ApiModelProperty("库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * 物品ID
	 */
	@ApiModelProperty("物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

	/**
	 * 包装ID
	 */
	@ApiModelProperty("包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;

	/**
	 * 补货数量
	 */
	@ApiModelProperty("补货数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal repQty;
}
