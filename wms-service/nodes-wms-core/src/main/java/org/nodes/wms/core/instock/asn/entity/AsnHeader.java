package org.nodes.wms.core.instock.asn.entity;

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
 * 收货单头表实体类
 *
 * @author zx
 * @since 2019-12-13
 */
@Data
@TableName(value = "asn_header")
@ApiModel(value = "AsnHeader对象", description = "收货单头表")
public class AsnHeader extends AttributeBase implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String INSTORE_TYPE_KEY = "instore_type";

	/**
	 * 收货单ID
	 */
	@TableId(value = "asn_bill_id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "收货单ID")
	private Long asnBillId;
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
	private String ownerCode;
	/**
	 * 单据编码
	 */
	@Length(max = 60, message = "单据编码最大长度为60位")
	@NotNull(message = "单据编码不能为空")
	@ApiModelProperty(value = "单据编码")
	private String asnBillNo;
	/**
	 * 单据状态
	 */
	@ApiModelProperty(value = "单据状态")
	private Integer asnBillState;
	/**
	 * 组合单据ID
	 */
	@ApiModelProperty(value = "组合单据ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long billGroupId;
	/**
	 * 单据种类编码
	 */
	@ApiModelProperty(value = "单据种类编码")
	@Length(max = 30, message = "单据种类编码最大长度为30位")
	@NotNull(message = "单据种类编码不能为空")
	private String billTypeCd;

	/**
	 * 上位系统单据唯一标识
	 */
	@ApiModelProperty(value = "上位系统单据唯一标识")
	@Length(max = 100, message = "上位系统单据唯一标识最大长度为100位")
	private String billKey;
	/**
	 * 上位系统单编号
	 */
	@ApiModelProperty(value = "上位系统单编号")
	@Length(max = 30, message = "上位系统单编号最大长度为30位")
	private String orderNo;
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
	 * 上位系统单据类型
	 */
	@ApiModelProperty(value = "上位系统单据类型")
	@Length(max = 30, message = "上位系统单据类型最大长度为30位")
	private String billType;
	/**
	 * 预计到货时间
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "预计到货时间")
	@NotNull(message = "预计到货时间不能为空")
	private LocalDateTime scheduledArrivalDate;
	/**
	 * 实际到货时间
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "实际到货时间")
	private LocalDateTime actualArrivalDate;
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
	 * 入库方式
	 */
	@ApiModelProperty(value = "入库方式")
	@NotNull(message = "入库方式不能为空")
	private Integer instoreType;
	/**
	 * 运单编号
	 */
	@ApiModelProperty(value = "运单编号")
	@Length(max = 50, message = "运单编号最大长度为50位")
	private String shipNo;
	/**
	 * 供应商编码
	 */
	@ApiModelProperty(value = "供应商编码")
	@Length(max = 50, message = "供应商编码最大长度为50位")
	private String sCode;
	/**
	 * 供应商名称
	 */
	@ApiModelProperty(value = "供应商名称")
	@Length(max = 300, message = "供应商名称最大长度为300位")
	private String sName;
	/**
	 * 供应商地址
	 */
	@ApiModelProperty(value = "供应商地址")
	@Length(max = 500, message = "供应商地址最大长度为500位")
	private String sAddress;
	/**
	 * 供应商联系人
	 */
	@ApiModelProperty(value = "供应商联系人")
	@Length(max = 50, message = "最大长度为50位")
	private String contact;
	/**
	 * 供应商联系电话
	 */
	@ApiModelProperty(value = "供应商联系电话")
	@Length(max = 50, message = "供应商联系电话最大长度为50位")
	private String phone;
	/**
	 * 出库单编码
	 */
	@ApiModelProperty(value = "出库单编码")
	@Length(max = 60, message = "出库单编码最大长度为六十位")
	private String soBillNo;
	/**
	 * WMS备注
	 */
	@ApiModelProperty(value = "WMS备注")
	@Length(max = 200, message = "WMS备注最大长度为200位")
	private String asnBillRemark;
	/**
	 * 同步状态
	 */
	@ApiModelProperty(value = "同步状态编码")
	//@NotNull(message = "同步状态不能为空")
	private Integer syncState;
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
	/**
	 * 创建类型
	 */
	@ApiModelProperty(value = "创建类型")
	private Integer createType;
	/**
	 * 过账人员
	 */
	@ApiModelProperty("过账人员")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long postUser;
	/**
	 * 过账时间
	 */
	@ApiModelProperty("过账时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime postTime;
	/**
	 * 过账方式
	 */
	@ApiModelProperty("过账方式")
	private Integer postState;
}
