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
 * 波次策略明细实体类
 *
 * @author NodeX
 * @since 2021-05-26
 */
@Data
@TableName("st_wellen_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WellenDetail对象", description = "波次策略明细")
public class WellenDetail extends BaseEntity implements Serializable  {

	private static final long serialVersionUID = 1L;

	/**
	 * 波次策略明细ID
	 */
	@ApiModelProperty(value = "波次策略明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	* 波次策略ID
	*/
		@ApiModelProperty(value = "波次策略ID")
		@JsonSerialize(using = ToStringSerializer.class)
		private Long wellenId;
	/**
	* 订单字段 字典code=st_wellen_field
	*/
		@ApiModelProperty(value = "订单字段 字典code=st_wellen_field")
		private Integer billField;
	/**
	* 操作符 字典code=st_wellen_operation
	*/
		@ApiModelProperty(value = "操作符 字典code=st_wellen_operation")
		private Integer operation;
	/**
	* 第一值
	*/
		@ApiModelProperty(value = "第一值")
		private Integer value1;
	/**
	* 第二值
	*/
		@ApiModelProperty(value = "第二值")
		private Integer value2;
	/**
	* 条件 字典code=st_wellen_criteria
	*/
		@ApiModelProperty(value = "条件 字典code=st_wellen_criteria")
		private Integer criteria;


}
