package org.nodes.wms.core.stock.core.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import java.math.BigDecimal;

/**
 * @description 非安全库存物品
 *
 * @author pengwei
 * @since 2020-07-03
 */
@Data
public class UnsafeStockSkuVO {

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
	 * 库存数量
	 */
	@ApiModelProperty("库存数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal stockQty;
	/**
	 * 库存低储
	 */
	@ApiModelProperty("库存低储")
	private Integer lowStorage;
	/**
	 * 库存高储
	 */
	@ApiModelProperty("库存高储")
	private Integer highStorage;
}
