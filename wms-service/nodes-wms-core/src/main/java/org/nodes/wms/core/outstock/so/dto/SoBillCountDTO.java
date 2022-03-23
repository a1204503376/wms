package org.nodes.wms.core.outstock.so.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import java.math.BigDecimal;

/**
 * @author pengwei
 * @program WmsCore
 * @description 订单量
 * @create 20200407
 */
@Data
public class SoBillCountDTO {

	/**
	 * 库房ID
	 */
	@ApiModelProperty("库房ID")
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
	 * 订单量
	 */
	@ApiModelProperty("订单量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal qty;
	/**
	 * 库存量
	 */
	@ApiModelProperty("库存量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal stockQty;
}
