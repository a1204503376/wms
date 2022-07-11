package org.nodes.wms.core.relenishment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 补货单明细表实体类
 *
 * @author zx
 * @since 2019-12-13
 */
@Data
@TableName("relenishment_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Detail对象", description = "补货单明细表")
public class RelDetail extends SkuLotBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 库存ID
	 */
	@ApiModelProperty(value = "库存ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
	/**
	* 补货单明细ID
	*/
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "补货单明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long relDetailId;
	/**
	* 补货单ID
	*/
	@ApiModelProperty(value = "补货单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long relBillId;
	/**
	 * 换算倍率
	 */
	@ApiModelProperty(value = "换算倍率")
	private Integer convertQty;
	/**
	* 单据编码
	*/
	@ApiModelProperty(value = "单据编码")
	@NotNull(message = "单据编码不能为空")
	@Length(max = 60,message = "单据编码最大长度为60位")
	private String relBillNo;
	/**
	 * 所属货主ID
	 */
	@ApiModelProperty(value = "所属货主ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;
	/**
	 * 包装ID
	 */
	@ApiModelProperty(value = "包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 所属货主编码
	 */
	@ApiModelProperty(value = "所属货主编码")
	@JsonSerialize(using = ToStringSerializer.class)
	private String woCode;
	/**
	 * 所属货主名称
	 */
	@ApiModelProperty(value = "所属货主名称")
	private String woName;
	/**
	* 物品ID
	*/
	@ApiModelProperty(value = "物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull(message = "物品ID不能为空")
	private Long skuId;
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
	* 补货数量
	*/
	@ApiModelProperty(value = "补货数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	@NotNull(message = "补货数量不能为空")
	@Digits(integer = 20 , fraction = 6, message = "补货数量整数位长度不能超过20,小数位不能超过6:relQty")
	private BigDecimal relQty;
	/**
	* 实际数量
	*/
	@ApiModelProperty(value = "实际数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	@Digits(integer = 20 , fraction = 6, message = "实际数量整数位长度不能超过20,小数位不能超过6:realQty")
	private BigDecimal realQty;

	/**
	* 从库房ID
	*/
	@ApiModelProperty(value = "从库房ID")
	@Length(max = 30,message = "最大长度为20位")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long fromWhId;
	/**
	 * 从库区ID
	 */
	@ApiModelProperty(value = "从库区ID")
	@Length(max = 30,message = "最大长度为20位")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long fromZoneId;
	/**
	 * 从库位ID
	 */
	@ApiModelProperty(value = "从库位ID")
	@Length(max = 30,message = "最大长度为20位")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long fromLocId;
	/**
	 * 从容器编码
	 */
	@ApiModelProperty(value = "从容器编码")
	@Length(max = 30,message = "最大长度为20位")
	private String fromLpnCode;
	/**
	 * 至库房ID
	 */
	@ApiModelProperty(value = "至库房ID")
	@Length(max = 30,message = "最大长度为20位")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long toWhId;
	/**
	 * 至库区ID
	 */
	@ApiModelProperty(value = "至库区ID")
	@Length(max = 30,message = "最大长度为20位")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long toZoneId;
	/**
	 * 至库位ID
	 */
	@ApiModelProperty(value = "至库位ID")
	@Length(max = 30,message = "最大长度为20位")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long toLocId;
	/**
	 * 至容器编码
	 */
	@ApiModelProperty(value = "至容器编码")
	@Length(max = 30,message = "最大长度为20位")
	private String toLpnCode;
	/**
	* 执行状态
	*/
	@ApiModelProperty(value = "执行状态")
	private Integer relStatus;
	/**
	 * 单位层级
	 */
	@ApiModelProperty(value = "单位层级")
	private Integer skuLevel;
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
