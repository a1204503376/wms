package org.nodes.wms.statistics.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import java.math.BigDecimal;

/**
 * @description 物品出库量汇总
 *
 * @author pengwei
 * @since 2020-07-03
 */
@Data
public class SkuOutstockSummaryVO {

	/**
	 * 库房ID
	 */
	@ApiModelProperty("库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;
	/**
	 * 物品ID
	 */
	@ApiModelProperty("物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
	/**
	 * 物品编码
	 */
	@ApiModelProperty("物品编码")
	private String skuCode;
	/**
	 * 物品名称
	 */
	@ApiModelProperty("物品名称")
	private String skuName;
	/**
	 * 出库量
	 */
	@ApiModelProperty("出库量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal qty;

	/**
	 * 计量单位名称
	 */
	@ApiModelProperty("计量单位名称")
	private String wsuName;
}
