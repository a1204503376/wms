package org.nodes.wms.core.billing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springblade.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 计费规则主表实体类
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BillingRuleHeader对象", description = "计费规则主表")
public class BillingRuleHeader extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ApiModelProperty("主键id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	/**
	 * 计费编码
	 */
	@ApiModelProperty(value = "计费编码")
	private String billingCode;
	/**
	 * 计费名称
	 */
	@ApiModelProperty(value = "计费名称")
	private String billingName;
	/**
	 * 所属库房id
	 */
	@ApiModelProperty(value = "所属库房id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 所属库房编码
	 */
	@ApiModelProperty(value = "所属库房编码")
	private String whCode;
	/**
	 * 所属库房名称
	 */
	@ApiModelProperty(value = "所属库房名称")
	private String whName;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;


}
