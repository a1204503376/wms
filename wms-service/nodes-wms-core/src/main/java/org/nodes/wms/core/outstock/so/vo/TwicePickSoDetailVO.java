package org.nodes.wms.core.outstock.so.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 二次分拣物品明细VO
 * @Author zx
 * @Date 2020/5/8
 **/
@Data
public class TwicePickSoDetailVO {
	/**
	 * 物品ID
	 */
	@ApiModelProperty("物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
	/**
	 * 物品名称
	 */
	@ApiModelProperty("物品名称")
	private String skuName;

	/**
	 * 物品规格
	 */
	@ApiModelProperty("物品规格")
	private String skuSpec;

	/**
	 * 是否序列号
	 */
	@ApiModelProperty("是否序列号")
	private Integer isSn;

	/**
	 * 源容器编码
	 */
	@ApiModelProperty("源容器编码")
	private String sourceLpnCode;

	/**
	 * 源库位id
	 */
	@ApiModelProperty("源库位id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long sourceLocId;

	/**
	 * 源库位编码
	 */
	@ApiModelProperty("源库位编码")
	private String sourceLocCode;

	/**
	 * 容器上剩余物品数量
	 */
	@ApiModelProperty("容器剩余数量")
	private BigDecimal lpnQty;

	/**
	 * 实际拣货数量
	 */
	@ApiModelProperty("实际拣货数量")
	private BigDecimal pickQty;

	/**
	 * 计划拣货数量
	 */
	@ApiModelProperty("计划拣货数量")
	private BigDecimal planQty;

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
	 * 单据明细ID
	 */
	@ApiModelProperty("单据明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long billDetailId;

	/**
	 * 序列号列表
	 */
	@ApiModelProperty("序列号列表")
	private List<String> serialList;
}
