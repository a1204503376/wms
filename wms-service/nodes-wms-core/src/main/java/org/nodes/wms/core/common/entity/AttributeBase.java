package org.nodes.wms.core.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.nodes.core.tool.utils.StringPool;
import org.springblade.core.tenant.mp.TenantEntity;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author pengwei
 * @Date 2021/4/9 15:19
 * @Description
 */
@Data
public class AttributeBase extends TenantEntity {

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

	public String getValue(Integer index) {
		switch (index) {
			case 1:
				return this.attribute1;
			case 2:
				return this.attribute2;
			case 3:
				return this.attribute3;
			case 4:
				return this.attribute4;
			case 5:
				return this.attribute5;
			case 6:
				return this.attribute6;
			case 7:
				return this.attribute7;
			case 8:
				return this.attribute8;
			case 9:
				return this.attribute9;
			case 10:
				return this.attribute10;
			default:
				return StringPool.EMPTY;
		}
	}
}
