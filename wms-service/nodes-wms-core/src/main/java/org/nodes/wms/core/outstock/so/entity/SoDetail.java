
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
import org.hibernate.validator.constraints.Length;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 出库管理明细实体类
 *
 * @author zhonglianshuai
 * @since 2020-02-10
 */
@Data
@TableName("so_detail")
@ApiModel(value = "SoDetail对象", description = "出库管理明细对象")
public class SoDetail extends SkuLotBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 出库单明细ID
	 */
	@ApiModelProperty(value = "出库单明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "so_detail_id", type = IdType.ASSIGN_ID)
	private Long soDetailId;
	/**
	 * 发货单ID
	 */
	@ApiModelProperty(value = "发货单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
	/**
	 * 单据编码
	 */
	@ApiModelProperty(value = "单据编码")
	private String soBillNo;
	/**
	 * 订单行号
	 */
	@ApiModelProperty(value = "订单行号")
	private String soLineNo;
	/**
	 * 单据种类编码
	 */
	@ApiModelProperty(value = "单据种类编码")
	private String billTypeCd;
	/**
	 * 上位系统单据唯一标识
	 */
	@ApiModelProperty(value = "上位系统单据唯一标识")
	private String billDetailKey;
	/**
	 * 物品ID
	 */
	@ApiModelProperty(value = "物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
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
	 * 物品分类
	 */
	@ApiModelProperty(value = "物品分类")
	@TableField(exist = false)
	private String skuType;
	/**
	 * 规格
	 */
	@ApiModelProperty(value = "规格")
	private String skuSpec;
	/**
	 * 换算倍率
	 */
	@ApiModelProperty(value = "换算倍率")
	private Integer convertQty;
	/**
	 * 计量单位编码
	 */
	@ApiModelProperty(value = "计量单位编码")
	private String umCode;
	/**
	 * 计量单位名称
	 */
	@ApiModelProperty(value = "计量单位名称")
	private String umName;
	/**
	 * 计量单位编码
	 */
	@ApiModelProperty(value = "计量单位编码")
	private String baseUmCode;
	/**
	 * 基础计量单位名称
	 */
	@ApiModelProperty(value = "基础计量单位名称")
	private String baseUmName;
	/**
	 * 计划数量
	 */
	@ApiModelProperty(value = "计划数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	@Digits(integer = 20 , fraction = 6, message = "计划数量整数位长度不能超过20,小数位不能超过6:planQty")
	private BigDecimal planQty;
	/**
	 * 实绩数量
	 */
	@ApiModelProperty(value = "实际数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	@Digits(integer = 20 , fraction = 6, message = "实际数量整数位长度不能超过20,小数位不能超过6:scanQty")
	private BigDecimal scanQty;
	/**
	 * 剩余数量
	 */
	@ApiModelProperty(value = "剩余数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	@Digits(integer = 20 , fraction = 6, message = "剩余数量整数位长度不能超过20,小数位不能超过6:surplusQty")
	private BigDecimal surplusQty;
	/**
	 * 批次号
	 */
	@ApiModelProperty(value = "批次号")
	private String lotNumber;
	/**
	 * 发货库房
	 */
	@ApiModelProperty(value = "发货库房")
	private String pickWhCode;
	/**
	 * 单价
	 */
	@ApiModelProperty(value = "单价")
	private BigDecimal detailPrice;
	/**
	 * 明细总金额
	 */
	@ApiModelProperty(value = "明细总金额")
	private BigDecimal detailAmount;
	/**
	 * 单据状态
	 */
	@ApiModelProperty(value = "单据状态")
	private Integer billDetailState;
	/**
	 * 波次ID
	 */
	@ApiModelProperty(value = "波次ID")
	private Long wellenId;
	/**
	 * 差异报告Id
	 */
	@ApiModelProperty(value = "差异报告Id")
	private Long wcrepId;
	/**
	 * 是否允许替代（0：不允许，1：允许）
	 */
	@ApiModelProperty(value = "是否允许替代（0：不允许，1：允许）")
	private Integer allowReplace;

	/**
	 * 上位系统单据唯一标识
	 */
	@ApiModelProperty(value = "上位系统单据唯一标识")
	@Length(max = 30,message = "最大长度为30位")
	private String billKey;
	/**
	 * 上位系统单编号
	 */
	@ApiModelProperty(value = "上位系统单编号")
	@Length(max = 30,message = "上位系统单编号最大长度为30位")
	private String orderNo;
	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;
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
}
