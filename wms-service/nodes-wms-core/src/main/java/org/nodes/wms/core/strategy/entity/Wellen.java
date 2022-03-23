package org.nodes.wms.core.strategy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springblade.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 波次策略实体类
 *
 * @author NodeX
 * @since 2021-05-26
 */
@Data
@TableName("st_wellen")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Wellen对象", description = "波次策略")
public class Wellen extends BaseEntity implements Serializable  {

	private static final long serialVersionUID = 1L;

	/**
	 * 波次策略ID
	 */
	@ApiModelProperty(value = "波次策略ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	* 订单上限
	*/
		@ApiModelProperty(value = "订单上限")
		private Integer billUpper;
	/**
	* 订单下限
	*/
		@ApiModelProperty(value = "订单下限")
		private Integer billLower;
	/**
	* 行数上限
	*/
		@ApiModelProperty(value = "行数上限")
		private Integer rowsUpper;
	/**
	* 行数下限
	*/
		@ApiModelProperty(value = "行数下限")
		private Integer rowsLower;
	/**
	* 物品上限
	*/
		@ApiModelProperty(value = "物品上限")
		private Integer skuUpper;
	/**
	* 物品下限
	*/
		@ApiModelProperty(value = "物品下限")
		private Integer skuLower;
	/**
	* 体积上限
	*/
		@ApiModelProperty(value = "体积上限")
		private Integer volumeUpper;
	/**
	* 体积下限
	*/
		@ApiModelProperty(value = "体积下限")
		private Integer volumeLower;
	/**
	* 重量上限
	*/
		@ApiModelProperty(value = "重量上限")
		private Integer weightUpper;
	/**
	* 重量下限
	*/
		@ApiModelProperty(value = "重量下限")
		private Integer weightLower;
	/**
	* 单位数量上限
	*/
		@ApiModelProperty(value = "单位数量上限")
		private Integer unitNumberUpper;
	/**
	* 单位数量下限
	*/
		@ApiModelProperty(value = "单位数量下限")
		private Integer unitNumberLower;


}
