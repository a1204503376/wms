package org.nodes.wms.core.billing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.ToString;
import org.springblade.core.mp.base.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 计费协议实体类
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BillingAgreement对象", description = "计费协议")
public class BillingAgreement extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ApiModelProperty("主键id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	/**
	 * 协议号
	 */
	@ApiModelProperty(value = "协议号")
	private String agreementNo;
	/**
	 * 描述
	 */
	@ApiModelProperty("描述")
	private String agreementDesc;
	/**
	 * 货主id
	 */
	@ApiModelProperty(value = "货主id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;
	/**
	 * 货主编码
	 */
	@ApiModelProperty(value = "货主编码")
	private String ownerCode;
	/**
	 * 货主名称
	 */
	@ApiModelProperty(value = "货主名称")
	private String ownerName;
	/**
	 * 生效日期
	 */
	@ApiModelProperty(value = "生效日期")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.PATTERN_DATE)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	private LocalDate effectiveDate;
	/**
	 * 解约日期
	 */
	@ApiModelProperty(value = "解约日期")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.PATTERN_DATE)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	private LocalDate terminationDate;
	/**
	 * 终止日期
	 */
	@ApiModelProperty(value = "终止日期")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.PATTERN_DATE)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	private LocalDate closingDate;
	/**
	 * 计费规则主表id
	 */
	@ApiModelProperty(value = "计费规则主表id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long ruleHeaderId;
	/**
	 * 税率
	 */
	@ApiModelProperty(value = "税率")
	private Integer taxRate;
	/**
	 * 折扣
	 */
	@ApiModelProperty(value = "折扣")
	private Integer discount;
	/**
	 * 执行状态
	 */
	@ApiModelProperty(value = "执行状态")
	private Integer executeState;
	/**
	 * 协议状态
	 */
	@ApiModelProperty(value = "协议状态")
	private Integer agreementState;
	/**
	 * 自动续约状态
	 */
	@ApiModelProperty(value = "自动续约状态")
	private Boolean isAuto;
	/**
	 * 续约次数
	 */
	@ApiModelProperty(value = "续约次数")
	private Integer agreementCount;


}
