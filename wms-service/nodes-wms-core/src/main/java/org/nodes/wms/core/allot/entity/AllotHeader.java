
package org.nodes.wms.core.allot.entity;

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
import org.hibernate.validator.constraints.Length;
import org.nodes.wms.core.common.entity.AttributeBase;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 调拨单头表实体类
 *
 * @author Wangjw
 * @since 2020-01-23
 */
@Data
@TableName("wms_allot_header")
@ApiModel(value = "AllotHeader对象", description = "调拨单头表")
public class AllotHeader extends AttributeBase implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 调拨单ID
	 */
	@TableId(value = "allot_bill_id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "调拨单ID")
	private Long allotBillId;
	/**
	 * 单据编码
	 */
	@ApiModelProperty(value = "单据编码")
	private String allotBillNo;
	/**
	 * 单据状态
	 */
	@ApiModelProperty(value = "单据状态")
	private Integer allotBillState;
	/**
	 * 上位系统单据唯一标识
	 */
	@ApiModelProperty(value = "上位系统单据唯一标识")
	private String billKey;
	/**
	 * 上位系统单编号
	 */
	@Length(max = 30, message = "字符长度不能超过30")
	@ApiModelProperty(value = "上位系统单编号")
	private String orderNo;
	/**
	 * 上位系统备注备注
	 */
	@Length(max = 200, message = "上位系统备注不能超过200")
	@ApiModelProperty(value = "上位系统备注")
	private String allotRemark;
	/**
	 * 上位系统最后更新时间
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "上位系统最后更新时间")
	private LocalDateTime lastUpdateDate;
	/**
	 * 上位系统订单创建时间
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "上位系统订单创建时间")
	private LocalDateTime preCreateDate;
	/**
	 * 货主ID
	 */
	@NotNull(message = "货主ID不能为空")
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "货主ID")
	private Long woId;
	/**
	 * 调出库房ID
	 */
	@NotNull(message = "调出库房ID不能为空")
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "调出库房ID")
	private Long sourceWhId;
	/**
	 * 调出库房编码
	 */
	@ApiModelProperty("调出库房编码")
	private String sourceWhCode;
	/**
	 * 调入库房ID
	 */
	@NotNull(message = "调入库房ID不能为空")
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "调入库房ID")
	private Long targetWhId;
	/**
	 * 调入库房编码
	 */
	@ApiModelProperty("调入库房编码")
	private String targetWhCode;
	/**
	 * 调出库房编码
	 */
	@NotNull(message = "调出库区类型不能为空")
	@ApiModelProperty(value = "调出库区类型")
	private Integer sourceZoneType;
	/**
	 * 收货单ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "收货单ID")
	private Long asnBillId;
	/**
	 * 发货单ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "发货单ID")
	private Long soBillId;
	/**
	 * WMS备注
	 */
	@Length(max = 200, message = "字符长度不能超过200")
	@ApiModelProperty(value = "WMS备注")
	private String allotBillRemark;
	/**
	 * 同步状态
	 */
	@ApiModelProperty(value = "同步状态")
	private Integer syncState;

}
