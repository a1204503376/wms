package org.nodes.wms.core.outstock.so.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description 订单量详细信息
 *
 * @author pengwei
 * @since 2020-07-31
 */
@Data
public class NeedSkuDetailVO {

	/**
	 * 库房ID
	 */
	@ApiModelProperty("库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * 出库单ID
	 */
	@ApiModelProperty("出库单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;

	/**
	 * 出库单编码
	 */
	@ApiModelProperty("出库单编码")
	@JsonSerialize(using = ToStringSerializer.class)
	private String soBillNo;

	/**
	 * 客户名称
	 */
	@JsonProperty("cName")
	@ApiModelProperty("客户名称")
	private String cName;

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
	 * 包装ID
	 */
	@ApiModelProperty("包装ID")
	private Long wspId;

	/**
	 * 包装名称
	 */
	@ApiModelProperty("包装名称")
	private String wspName;

	/**
	 * 包装规格
	 */
	@ApiModelProperty("包装规格")
	private String spec;

	/**
	 * 订单量
	 */
	@ApiModelProperty("订单量")
	private BigDecimal qty;

	/**
	 * 计量单位名称
	 */
	@ApiModelProperty("计量单位名称")
	private String wsuName;
}
