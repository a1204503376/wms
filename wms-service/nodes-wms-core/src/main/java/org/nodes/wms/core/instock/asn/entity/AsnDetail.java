package org.nodes.wms.core.instock.asn.entity;

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
import org.hibernate.validator.constraints.Length;
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收货单明细表实体类
 *
 * @author zx
 * @since 2019-12-13
 */
@Data
@TableName("asn_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Detail对象", description = "收货单明细表")
public class AsnDetail extends SkuLotBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	* 收货单明细ID
	*/
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "收货单明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long asnDetailId;
	/**
	* 收货单ID
	*/
	@ApiModelProperty(value = "收货单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long asnBillId;
	/**
	* 单据编码
	*/
	@ApiModelProperty(value = "单据编码")
	@NotNull(message = "单据编码不能为空")
	@Length(max = 60,message = "单据编码最大长度为60位")
	private String asnBillNo;
	/**
	* 订单行号
	*/
	@ApiModelProperty(value = "订单行号")
	@NotNull(message = "订单行号不能为空")
	@Length(max = 50,message = "订单行号最大长度为50位")
	private String asnLineNo;
	/**
	* 上位系统单据明细唯一标识
	*/
	@ApiModelProperty(value = "上位系统单据明细唯一标识")
	@Length(max = 30,message = "最大长度为30位")
	private String asnBillDetailKey;
	/**
	* 物品ID
	*/
	@ApiModelProperty(value = "物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull(message = "物品ID不能为空")
	private Long skuId;
	/**
	* 包装ID
	*/
	@ApiModelProperty(value = "包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull(message = "包装ID不能为空")
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
	@NotNull(message = "物品编码不能为空")
	@Length(max = 30,message = "最大长度为30位")
	private String skuCode;
	/**
	* 物品名称
	*/
	@ApiModelProperty(value = "物品名称")
	@NotNull(message = "物品名称不能为空")
	@Length(max = 30,message = "最大长度为30位")
	private String skuName;
	/**
	* 规格
	*/
	@ApiModelProperty(value = "规格")
	@Length(max = 30,message = "最大长度为30位")
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
	@Length(max = 30,message = "最大长度为30位")
	@NotNull(message = "计量单位编码不能为空")
	private String umCode;
	/**
	* 计量单位名称
	*/
	@ApiModelProperty(value = "计量单位名称")
	@NotNull(message = "计量单位名称不能为空")
	@Length(max = 30,message = "最大长度为30位")
	private String umName;
	/**
	* 基础计量单位编码
	*/
	@ApiModelProperty(value = "基础计量单位编码")
	@NotNull(message = "基础计量单位编码不能为空")
	@Length(max = 30,message = "最大长度为30位")
	private String baseUmCode;
	/**
	* 基础计量单位名称
	*/
	@ApiModelProperty(value = "基础计量单位名称")
	@NotNull(message = "基础计量单位名称不能为空")
	@Length(max = 30,message = "最大长度为30位")
	private String baseUmName;
	/**
	* 计划数量
	*/
	@ApiModelProperty(value = "计划数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	@NotNull(message = "计划数量不能为空")
	@Digits(integer = 20 , fraction = 6, message = "计划数量整数位长度不能超过20,小数位不能超过6:planQty")
	private BigDecimal planQty;
	/**
	* 实际数量
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
	* 收货库房
	*/
	@ApiModelProperty(value = "收货库房")
	@Length(max = 30,message = "最大长度为30位")
	private String incomeWhCode;
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
	* 上位系统备注
	*/
	@ApiModelProperty(value = "上位系统备注")
	@Length(max = 30,message = "最大长度为30位")
	private String asnDetailRemark;
	/**
	* 接收状态
	*/
	@ApiModelProperty(value = "接收状态")
	private Integer detailStatus;
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
	 * 差异报告Id
	 */
	@ApiModelProperty(value = "差异报告Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wcrepId;

	/**
	 * 序列号导入状态
	 */
	@ApiModelProperty(value = "序列号导入状态")
	private String importSn;
	/**
	 * 物品分类
	 */
	@ApiModelProperty(value = "物品分类")
	@TableField(exist = false)
	private String skuType;
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
