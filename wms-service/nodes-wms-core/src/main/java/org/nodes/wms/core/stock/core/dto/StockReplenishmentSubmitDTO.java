package org.nodes.wms.core.stock.core.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import java.math.BigDecimal;

/**
 * 补货提交DTO
 * @Author zx
 * @Date 2020/8/4
 **/
@Data
public class StockReplenishmentSubmitDTO {

	/**
	 * 补货任务明细ID
	 */
	@ApiModelProperty("补货任务明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long relDetailId;
	/**
	 * 物品ID
	 */
	@ApiModelProperty("物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
	/**
	 * 货主ID
	 */
	@ApiModelProperty("货主ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;
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
	private BigDecimal realQty;

	/**
	 * 目标库位编码
	 */
	@ApiModelProperty("目标库位编码")
	private String targetLocCode;
	/**
	 * 目标库区ID
	 */
	@ApiModelProperty("目标库区ID")
	private Long targetZoneId;
	/**
	 * 目标容器编码
	 */
	@ApiModelProperty("目标容器编码")
	private String targetLpnCode;

	/**
	 * 计量单位名称
	 */
	@ApiModelProperty(value = "计量单位名称")
	private String umName;
}
