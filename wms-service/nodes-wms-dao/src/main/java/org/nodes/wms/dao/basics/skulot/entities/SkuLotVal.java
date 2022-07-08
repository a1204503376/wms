
package org.nodes.wms.dao.basics.skulot.entities;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 批属性验证实体类
 *
 * @author chenhz
 * @since 2019-12-18
 */
@Data
@TableName("wms_sku_lot_val")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "SkuLotVal对象", description = "批属性验证")
public class SkuLotVal extends SkuLotValBaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 批属性验证ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "批属性验证ID")
	@TableId(value = "wslv_id", type = IdType.ASSIGN_ID)
	private Long wslvId;
	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "库房ID")
	private Long whId;
	/**
	 * 货主ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "货主ID")
	private Long woId;
	/**
	 * 批属性验证名称
	 */
	@ApiModelProperty(value = "批属性验证名称")
	@Length(max = 180, message = "批属性验证名称最大长度为180位")
	@NotNull(message = "批属性验证名称不能为空")
	private String skuLotValName;
	/**
	 * 批属性说明
	 */
	@ApiModelProperty(value = "批属性说明")
	@Length(max = 200, message = "批属性说明最大长度为200位")
	private String skuLotRemark;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust1;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust2;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust3;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust4;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust5;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust6;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust7;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust8;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust9;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust10;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust11;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust12;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust13;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust14;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust15;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust16;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust17;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust18;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust19;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust20;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust21;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust22;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust23;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust24;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust25;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust26;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust27;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust28;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust29;
	/**
	 * 0：非必须，1：必须
	 */
	@ApiModelProperty(value = "0：非必须，1：必须")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMust30;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp1;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp2;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp3;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp4;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp5;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp6;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp7;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp8;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp9;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp10;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp11;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp12;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp13;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp14;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp15;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp16;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp17;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp18;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp19;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp20;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp21;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp22;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp23;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp24;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp25;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp26;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp27;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp28;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp29;
	/**
	 * 0：不可见，1：可见  注：必须=1，固定设置为可见
	 */
	@ApiModelProperty(value = "0：不可见，1：可见  注：必须=1，固定设置为可见")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotDisp30;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix1;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix2;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix3;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix4;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix5;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix6;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix7;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix8;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix9;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix10;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix11;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix12;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix13;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix14;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix15;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix16;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix17;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix18;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix19;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix20;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix21;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix22;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix23;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix24;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix25;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix26;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix27;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix28;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix29;
	/**
	 * 混合存放0：不允许混合，1：允许混合
	 */
	@ApiModelProperty(value = "混合存放0：不允许混合，1：允许混合")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMix30;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask1;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask2;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask3;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask4;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask5;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask6;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask7;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask8;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask9;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask10;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask11;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask12;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask13;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask14;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask15;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask16;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask17;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask18;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask19;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask20;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask21;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask22;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask23;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask24;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask25;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask26;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask27;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask28;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask29;
	/**
	 * 混合存放掩码
	 */
	@ApiModelProperty(value = "混合存放掩码")
	@Length(max = 500, message = "混合存放掩码最大长度为500位")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String skuLotMixMask30;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask1;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask2;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask3;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask4;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask5;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask6;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask7;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask8;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask9;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask10;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask11;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask12;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask13;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask14;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask15;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask16;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask17;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask18;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask19;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask20;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask21;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask22;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask23;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask24;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask25;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask26;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask27;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask28;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask29;
	/**
	 * 批属性生成掩码
	 */
	@ApiModelProperty(value = "批属性生成掩码")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotMask30;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType1;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType2;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType3;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType4;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType5;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType6;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType7;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType8;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType9;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType10;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType11;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType12;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType13;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType14;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType15;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType16;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType17;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType18;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType19;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType20;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType21;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType22;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType23;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType24;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType25;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType26;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType27;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType28;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType29;
	/**
	 * 批掩码生成规则
	 */
	@ApiModelProperty(value = "批掩码生成规则")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLotEditType30;
	/**
	 * 长度限制1
	 */
	@ApiModelProperty("长度限制1")
	@Range(max = 99, min = 0, message="长度限制1 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen1;
	/**
	 * 长度限制2
	 */
	@ApiModelProperty("长度限制2")
	@Range(max = 99, min = 0, message="长度限制2 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen2;
	/**
	 * 长度限制3
	 */
	@ApiModelProperty("长度限制3")
	@Range(max = 99, min = 0, message="长度限制3 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen3;
	/**
	 * 长度限制4
	 */
	@ApiModelProperty("长度限制4")
	@Range(max = 99, min = 0, message="长度限制4 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen4;
	/**
	 * 长度限制5
	 */
	@ApiModelProperty("长度限制5")
	@Range(max = 99, min = 0, message="长度限制5 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen5;
	/**
	 * 长度限制6
	 */
	@ApiModelProperty("长度限制6")
	@Range(max = 99, min = 0, message="长度限制6 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen6;
	/**
	 * 长度限制7
	 */
	@ApiModelProperty("长度限制7")
	@Range(max = 99, min = 0, message="长度限制7 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen7;
	/**
	 * 长度限制8
	 */
	@ApiModelProperty("长度限制8")
	@Range(max = 99, min = 0, message="长度限制8 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen8;
	/**
	 * 长度限制9
	 */
	@ApiModelProperty("长度限制9")
	@Range(max = 99, min = 0, message="长度限制9 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen9;
	/**
	 * 长度限制10
	 */
	@ApiModelProperty("长度限制10")
	@Range(max = 99, min = 0, message="长度限制10 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen10;
	/**
	 * 长度限制11
	 */
	@ApiModelProperty("长度限制11")
	@Range(max = 99, min = 0, message="长度限制11 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen11;
	/**
	 * 长度限制12
	 */
	@ApiModelProperty("长度限制12")
	@Range(max = 99, min = 0, message="长度限制12 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen12;
	/**
	 * 长度限制13
	 */
	@ApiModelProperty("长度限制13")
	@Range(max = 99, min = 0, message="长度限制13 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen13;
	/**
	 * 长度限制14
	 */
	@ApiModelProperty("长度限制14")
	@Range(max = 99, min = 0, message="长度限制14 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen14;
	/**
	 * 长度限制15
	 */
	@ApiModelProperty("长度限制15")
	@Range(max = 99, min = 0, message="长度限制15 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen15;
	/**
	 * 长度限制16
	 */
	@ApiModelProperty("长度限制16")
	@Range(max = 99, min = 0, message="长度限制16 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen16;
	/**
	 * 长度限制17
	 */
	@ApiModelProperty("长度限制17")
	@Range(max = 99, min = 0, message="长度限制17 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen17;
	/**
	 * 长度限制18
	 */
	@ApiModelProperty("长度限制18")
	@Range(max = 99, min = 0, message="长度限制18 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen18;
	/**
	 * 长度限制19
	 */
	@ApiModelProperty("长度限制19")
	@Range(max = 99, min = 0, message="长度限制19 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen19;
	/**
	 * 长度限制20
	 */
	@ApiModelProperty("长度限制20")
	@Range(max = 99, min = 0, message="长度限制20 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen20;
	/**
	 * 长度限制21
	 */
	@ApiModelProperty("长度限制21")
	@Range(max = 99, min = 0, message="长度限制21 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen21;
	/**
	 * 长度限制22
	 */
	@ApiModelProperty("长度限制22")
	@Range(max = 99, min = 0, message="长度限制22 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen22;
	/**
	 * 长度限制23
	 */
	@ApiModelProperty("长度限制23")
	@Range(max = 99, min = 0, message="长度限制23 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen23;
	/**
	 * 长度限制24
	 */
	@ApiModelProperty("长度限制24")
	@Range(max = 99, min = 0, message="长度限制24 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen24;
	/**
	 * 长度限制25
	 */
	@ApiModelProperty("长度限制25")
	@Range(max = 99, min = 0, message="长度限制25 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen25;
	/**
	 * 长度限制26
	 */
	@ApiModelProperty("长度限制26")
	@Range(max = 99, min = 0, message="长度限制26 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen26;
	/**
	 * 长度限制27
	 */
	@ApiModelProperty("长度限制27")
	@Range(max = 99, min = 0, message="长度限制27 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen27;
	/**
	 * 长度限制28
	 */
	@ApiModelProperty("长度限制28")
	@Range(max = 99, min = 0, message="长度限制28 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen28;
	/**
	 * 长度限制29
	 */
	@ApiModelProperty("长度限制29")
	@Range(max = 99, min = 0, message="长度限制29 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen29;
	/**
	 * 长度限制30
	 */
	@ApiModelProperty("长度限制30")
	@Range(max = 99, min = 0, message="长度限制30 的值应介于0~99之间")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer skuLen30;


}
