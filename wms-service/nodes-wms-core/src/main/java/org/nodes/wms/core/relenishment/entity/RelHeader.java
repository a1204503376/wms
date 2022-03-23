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
import org.hibernate.validator.constraints.Length;
import org.nodes.wms.core.common.entity.AttributeBase;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 补货单头表实体类
 *
 * @author whj
 * @since 2019-12-13
 */
@Data
@TableName(value = "relenishment_header")
@ApiModel(value = "RelHeader对象", description = "补货单头表")
public class RelHeader extends AttributeBase implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 补货单ID
	 */
	@TableId(value = "rel_bill_id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "补货单ID")
	private Long relBillId;
	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "库房ID")
	@NotNull(message = "库房id不能为空")
	private Long whId;
	/**
	 * 库房编码
	 */
	@ApiModelProperty(value = "库房编码")
	@Length(max = 30,message = "库房编码最大长度为30位")
	private String whCode;
	/**
	 * 货主ID
	 */
	@JsonSerialize(using = ToStringSerializer.class )
	@ApiModelProperty(value = "货主ID")
	@NotNull(message = "货主id不能为空")
	private Long woId;
	/**
	 * 货主编码
	 */
	@Length(max = 50, message = "货主编码最大长度为50位")
	@ApiModelProperty(value = "货主编码")
	private String woCode;
	/**
	 * 货主名称
	 */
	@Length(max = 50, message = "货主名称最大长度为50位")
	@ApiModelProperty(value = "货主名称")
	private String woName;
	/**
	 * 单据编码
	 */
	@Length(max = 60, message = "单据编码最大长度为60位")
	@NotNull(message = "单据编码不能为空")
	@ApiModelProperty(value = "单据编码")
	private String relBillNo;
	/**
	 * 单据状态
	 */
	@ApiModelProperty(value = "单据状态")
	private Integer relBillState;
	/**
	 * 单据完成时间
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "单据完成时间")
	private LocalDateTime finishDate;
	/**
	 * WMS备注
	 */
	@ApiModelProperty(value = "WMS备注")
	@Length(max = 200, message = "WMS备注最大长度为200位")
	private String relBillRemark;
	/**
	 * 部门ID
	 */
	@ApiModelProperty(value = "部门ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long deptId;
	/**
	 * 部门编码
	 */
	@ApiModelProperty(value = "部门编码")
	@Length(max = 30, message = "部门编码最大长度为30位")
	private String deptCode;
	/**
	 * 部门名称
	 */
	@ApiModelProperty(value = "部门名称")
	@Length(max = 200, message = "部门名称最大长度为200位")
	private String deptName;
}
