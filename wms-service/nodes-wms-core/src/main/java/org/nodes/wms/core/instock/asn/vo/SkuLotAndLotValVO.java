package org.nodes.wms.core.instock.asn.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 批属性标签与批属性验证VO
 *
 * @Author zx
 * @Date 2019/12/27
 **/
@Data
@ApiModel(value = "批属性标签与批属性验证VO对象", description = "批属性标签与批属性验证")
public class SkuLotAndLotValVO {
	/**
	 * 物品批属性索引
	 */
	@ApiModelProperty(value = "物品批属性索引")
	private String skuLotIndex;

	/**
	 * 物品批属性内容
	 */
	@ApiModelProperty(value = "物品批属性内容")
	private String skuLotValue;

	/**
	 * 物品批属性
	 */
	@ApiModelProperty(value = "物品批属性")
	private String skuLot;
	/**
	 * 物品批属性标签
	 */
	@ApiModelProperty(value = "物品批属性标签")
	private String skuLotLabel;
	/**
	 * 必填验证
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	private Integer skuLotMust;
	/**
	 * 隐藏验证
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	private Integer skuLotDisp;
	/**
	 * 混合存放skuLotMust
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	private Integer skuLotMix;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	private String skuLotMixMask;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	private Integer skuLotMask;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	private Integer skuLotEditType;
	/**
	 * 长度限制
	 */
	@ApiModelProperty(value = "长度限制")
	private BigDecimal skuLen;

	/**
	 * 输入框类型
	 */
	@ApiModelProperty(value = "输入框类型")
	private Integer skuTextType;


	/**
	 * 批属性掩码备注
	 */
	@ApiModelProperty("批属性掩码备注")
	private String skuLotMaskRemark;

	/**
	 * 批属性值
	 */
	@ApiModelProperty("批属性掩码备注")
	private String skuLotMaskValue;
}
