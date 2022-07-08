package org.nodes.wms.core.stock.core.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import java.math.BigDecimal;

/**
 * @description 标签打印信息
 *
 * @author pengwei
 * @since 2020-07-15
 */
@Data
public class PrintLabelInfoDTO extends SkuLotBaseEntity {

	/**
	 * 标签类型，对应字典表
	 */
	@ApiModelProperty("标签类型，对应字典表")
	private Integer labelType;

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
	 * 数量
	 */
	@ApiModelProperty("数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal qty;

	/**
	 * 计量单位名称
	 */
	@ApiModelProperty("计量单位名称")
	private String wsuName;

	/**
	 * 批属性1：生产批次
	 */
	@ApiModelProperty("生产批次")
	private String skuLot1;

	/**
	 * 批属性2：入库日期
	 */
	@ApiModelProperty("入库日期")
	private String skuLot2;

	/**
	 * 批属性3：生产日期
	 */
	@ApiModelProperty("生产日期")
	private String skuLot3;

	/**
	 * 批属性4：备注
	 */
	@ApiModelProperty("备注")
	private String skuLot4;

	/**
	 * 批属性5：类型标识
	 */
	@ApiModelProperty("类型标识")
	private String skuLot5;

	/**
	 * 二维码内容
	 */
	@ApiModelProperty("二维码内容")
	private String barcode;

}
