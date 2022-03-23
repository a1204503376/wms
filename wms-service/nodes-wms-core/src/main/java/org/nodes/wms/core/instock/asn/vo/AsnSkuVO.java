package org.nodes.wms.core.instock.asn.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收货物品VO
 * @Author zx
 * @Date 2020/8/7
 **/
@Data
public class AsnSkuVO {

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
