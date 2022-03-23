package org.nodes.wms.core.billing.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.springblade.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 计费规则明细实体类
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BillingRuleDetail对象", description = "计费规则明细")
public class BillingRuleDetail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ApiModelProperty("主键id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	/**
	 * 主表id
	 */
	@ApiModelProperty(value = "主表id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long headerId;
	/**
	 * 行号
	 */
	@ApiModelProperty(value = "行号")
	private String lineNo;
	/**
	 * 计费项目id
	 */
	@ApiModelProperty(value = "计费项目id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long itemId;
	/**
	 * 计费项目编码
	 */
	@ApiModelProperty(value = "计费项目编码")
	private String itemCode;
	/**
	 * 计费项目名称
	 */
	@ApiModelProperty(value = "计费项目名称")
	private String itemName;
	/**
	 * 金额
	 */
	@ApiModelProperty(value = "金额")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal amount;
	/**
	 * 计量单位id
	 */
	@ApiModelProperty(value = "计量单位id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wsuId;
	/**
	 * 计量单位编码
	 */
	@ApiModelProperty(value = "计量单位编码")
	private String wsuCode;
	/**
	 * 计量单位名称
	 */
	@ApiModelProperty(value = "计量单位名称")
	private String wsuName;


}
