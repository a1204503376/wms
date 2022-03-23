package org.nodes.wms.core.stock.core.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 补货物品VO
 * @Author zx
 * @Date 2020/8/5
 **/
@Data
public class StockReplenishmentSkuVO {
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
	 * 规格
	 */
	@ApiModelProperty("规格")
	private String skuSpec;

	/**
	 * 货主ID
	 */
	@ApiModelProperty("货主ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;

	/**
	 * 货主名称
	 */
	@ApiModelProperty("货主名称")
	private String ownerName;

	/**
	 * 包装ID
	 */
	@ApiModelProperty("包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;

	/**
	 * 包装名称
	 */
	@ApiModelProperty("包装名称")
	private String wspName;


}
