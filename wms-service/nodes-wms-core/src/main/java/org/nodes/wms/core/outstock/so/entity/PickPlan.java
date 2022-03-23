
package org.nodes.wms.core.outstock.so.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 拣货计划表实体类
 *
 * @author pengwei
 * @since 2020-02-10
 */
@Data
@TableName("so_pick_plan")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PickPlan对象", description = "拣货计划表")
public class PickPlan extends SkuLotBaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 拣货计划ID
	 */
	@ApiModelProperty(value = "拣货计划ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "pick_plan_id", type = IdType.ASSIGN_ID)
	private Long pickPlanId;
	/**
	 * 波次ID
	 */
	@ApiModelProperty(value = "波次ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wellenId;
	/**
	 * 库存ID
	 */
	@ApiModelProperty("库存ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
	/**
	 * 库位ID
	 */
	@ApiModelProperty(value = "库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;
	/**
	 * 库位编码
	 */
	@ApiModelProperty(value = "库位编码")
	private String locCode;
	/**
	 * 物品ID
	 */
	@ApiModelProperty(value = "物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
	/**
	 * 物品编码
	 */
	@ApiModelProperty(value = "物品编码")
	private String skuCode;
	/**
	 * 物品名称
	 */
	@ApiModelProperty(value = "物品名称")
	private String skuName;
	/**
	 * 被替代物品ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty("被替代物品ID")
	private Long repSkuId;
	/**
	 * 被替代物品编码
	 */
	@ApiModelProperty("被替代物品编码")
	private String repSkuCode;
	/**
	 * 被替代物品名称
	 */
	@ApiModelProperty("被替代物品名称")
	private String repSkuName;
	/**
	 * 批次号
	 */
	@ApiModelProperty(value = "批次号")
	private String lotNumber;
	/**
	 * 包装ID
	 */
	@ApiModelProperty(value = "包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 层级
	 */
	@ApiModelProperty(value = "层级")
	private Integer skuLevel;
	/**
	 * 计划量
	 */
	@ApiModelProperty(value = "计划量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal pickPlanQty;
	/**
	 * 拣货量
	 */
	@ApiModelProperty(value = "拣货量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal pickRealQty;
	/**
	 * 待拣货量
	 * 按单拣货
	 */
	@ApiModelProperty(value = "待拣货量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	@TableField(exist = false)
	private BigDecimal unPlanQty;
	/**
	 * 任务ID
	 */
	@ApiModelProperty(value = "任务ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long taskId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 备用字段1
	 */
	@ApiModelProperty("备用字段1")
	private String attribute1;
	/**
	 * 备用字段2
	 */
	@ApiModelProperty("备用字段2")
	private String attribute2;
	/**
	 * 备用字段3
	 */
	@ApiModelProperty("备用字段3")
	private String attribute3;
	/**
	 * 备用字段4
	 */
	@ApiModelProperty("备用字段4")
	private String attribute4;
	/**
	 * 备用字段5
	 */
	@ApiModelProperty("备用字段5")
	private String attribute5;
	/**
	 * 备用字段6
	 */
	@ApiModelProperty("备用字段6")
	private String attribute6;
	/**
	 * 备用字段7
	 */
	@ApiModelProperty("备用字段7")
	private String attribute7;
	/**
	 * 备用字段8
	 */
	@ApiModelProperty("备用字段8")
	private String attribute8;
	/**
	 * 备用字段9
	 */
	@ApiModelProperty("备用字段9")
	private String attribute9;
	/**
	 * 备用字段10
	 */
	@ApiModelProperty("备用字段10")
	private String attribute10;
	/**
	 * 备用字段11
	 */
	@ApiModelProperty("备用字段11")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal attribute11;
	/**
	 * 备用字段12
	 */
	@ApiModelProperty("备用字段12")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal attribute12;
	/**
	 * 备用字段13
	 */
	@ApiModelProperty("备用字段13")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal attribute13;
	/**
	 * 备用字段14
	 */
	@ApiModelProperty("备用字段14")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal attribute14;
	/**
	 * 备用字段15
	 */
	@ApiModelProperty("备用字段15")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal attribute15;
	/**
	 * 备用字段16
	 */
	@ApiModelProperty("备用字段16")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal attribute16;
	/**
	 * 备用字段17
	 */
	@ApiModelProperty("备用字段17")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal attribute17;
	/**
	 * 备用字段18
	 */
	@ApiModelProperty("备用字段18")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal attribute18;
	/**
	 * 备用字段19
	 */
	@ApiModelProperty("备用字段19")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal attribute19;
	/**
	 * 备用字段20
	 */
	@ApiModelProperty("备用字段20")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal attribute20;
	/**
	 * 备用字段21
	 */
	@ApiModelProperty("备用字段21")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime attribute21;
	/**
	 * 备用字段22
	 */
	@ApiModelProperty("备用字段22")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime attribute22;
	/**
	 * 备用字段23
	 */
	@ApiModelProperty("备用字段23")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime attribute23;
	/**
	 * 备用字段24
	 */
	@ApiModelProperty("备用字段24")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime attribute24;
	/**
	 * 备用字段25
	 */
	@ApiModelProperty("备用字段25")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime attribute25;
	/**
	 * 备用字段26
	 */
	@ApiModelProperty("备用字段26")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime attribute26;
	/**
	 * 备用字段27
	 */
	@ApiModelProperty("备用字段27")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime attribute27;
	/**
	 * 备用字段28
	 */
	@ApiModelProperty("备用字段28")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime attribute28;
	/**
	 * 备用字段29
	 */
	@ApiModelProperty("备用字段29")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime attribute29;
	/**
	 * 备用字段30
	 */
	@ApiModelProperty("备用字段30")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime attribute30;
	/**
	 * 出库单编码
	 */
	@ApiModelProperty("出库单编码")
	private String soBillNo;
}
