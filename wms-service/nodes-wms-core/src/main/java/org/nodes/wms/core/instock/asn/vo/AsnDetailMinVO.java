package org.nodes.wms.core.instock.asn.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import java.math.BigDecimal;

/**
 * 收货单明细表手持VO
 *
 * @Author zx
 * @Date 2020/1/13
 **/
@Data
public class AsnDetailMinVO {
	/**
	 * 计划数量
	 */
	@ApiModelProperty(value = "计划总数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal planCountQty;

	/**
	 * 实绩数量
	 */
	@ApiModelProperty(value = "实绩总数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal scanCountQty;

	/**
	 * 计划数量
	 */
	@ApiModelProperty(value = "计划数量名")
	private String planQtyName;

	/**
	 * 实绩数量名
	 */
	@ApiModelProperty(value = "实绩数量名")
	private String scanQtyName;


	/**
	 * 明细单完成数
	 */
	@ApiModelProperty(value = "明细单完成数")
	private Integer finish;

	/**
	 * 明细单总数
	 */
	@ApiModelProperty(value = "明细单总数")
	private Integer count;

	/**
	 * 物品名称
	 */
	@ApiModelProperty(value = "物品名称")
	private String skuName;

	/**
	 * 接收状态
	 */
	@ApiModelProperty(value = "接收状态")
	private String detailStatusName;

	/**
	 * 规格
	 */
	@ApiModelProperty(value = "规格")
	private String skuSpec;
}
