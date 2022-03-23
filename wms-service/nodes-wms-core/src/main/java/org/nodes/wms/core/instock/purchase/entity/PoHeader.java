package org.nodes.wms.core.instock.purchase.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.nodes.wms.core.common.entity.AttributeBase;
import org.springblade.core.mp.base.BaseEntity;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 采购单头表实体类
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PoHeader对象", description = "采购单头表")
public class PoHeader extends AttributeBase implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 采购单ID
	 */
	@ApiModelProperty(value = "采购单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "po_bill_id", type= IdType.ASSIGN_ID)
	private Long poBillId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 库房编码
	 */
	@ApiModelProperty(value = "库房编码")
	private String whCode;
	/**
	 * 货主ID
	 */
	@ApiModelProperty(value = "货主ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;
	/**
	 * 货主编码
	 */
	@ApiModelProperty(value = "货主编码")
	private String ownerCode;
	/**
	 * 单据编码
	 */
	@ApiModelProperty(value = "单据编码")
	private String poBillNo;
	/**
	 * 单据状态 10：单据创建、20：处理中、30：部分收货、40：已完成、90：已撤销
	 */
	@ApiModelProperty(value = "单据状态 10：单据创建、20：处理中、30：部分收货、40：已完成、90：已撤销")
	private Integer poBillState;
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
	private String billTypeCd;
	/**
	 * 上位系统单据唯一标识
	 */
	@ApiModelProperty(value = "上位系统单据唯一标识")
	private String billKey;
	/**
	 * 上位系统单编号
	 */
	@ApiModelProperty(value = "上位系统单编号")
	private String orderNo;
	/**
	 * 上位系统最后更新时间
	 */
	@ApiModelProperty(value = "上位系统最后更新时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime lastUpdateDate;
	/**
	 * 上位系统订单创建时间
	 */
	@ApiModelProperty(value = "上位系统订单创建时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime preCreateDate;
	/**
	 * 上位系统单据类型
	 */
	@ApiModelProperty(value = "上位系统单据类型")
	private String billType;
	/**
	 * 预计到货时间
	 */
	@ApiModelProperty(value = "预计到货时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime scheduledArrivalDate;
	/**
	 * 实际到货时间
	 */
	@ApiModelProperty(value = "实际到货时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime actualArrivalDate;
	/**
	 * 单据完成时间
	 */
	@ApiModelProperty(value = "单据完成时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime finishDate;
	/**
	 * 入库方式
	 */
	@ApiModelProperty(value = "入库方式")
	private Integer instoreType;
	/**
	 * 运单编号
	 */
	@ApiModelProperty(value = "运单编号")
	private String shipNo;
	/**
	 * 供应商编码
	 */
	@ApiModelProperty(value = "供应商编码")
	private String sCode;
	/**
	 * 供应商名称
	 */
	@JsonProperty("sName")
	@ApiModelProperty(value = "供应商名称")
	private String sName;
	/**
	 * 供应商地址
	 */
	@JsonProperty("sAddress")
	@ApiModelProperty(value = "供应商地址")
	private String sAddress;
	/**
	 * 供应商联系人
	 */
	@ApiModelProperty(value = "供应商联系人")
	private String contact;
	/**
	 * 供应商联系电话
	 */
	@ApiModelProperty(value = "供应商联系电话")
	private String phone;
	/**
	 * 出库单编码
	 */
	@ApiModelProperty(value = "出库单编码")
	private String soBillNo;
	/**
	 * WMS备注
	 */
	@ApiModelProperty(value = "WMS备注")
	private String asnBillRemark;
	/**
	 * 同步状态
	 */
	@ApiModelProperty(value = "同步状态")
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
	private String deptCode;
	/**
	 * 部门名称
	 */
	@ApiModelProperty(value = "部门名称")
	private String deptName;
	/**
	 * 创建类型
	 */
	@ApiModelProperty(value = "创建类型")
	private Integer createType;
	/**
	 * 过账人员
	 */
	@ApiModelProperty(value = "过账人员")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long postUser;
	/**
	 * 过账时间
	 */
	@ApiModelProperty(value = "过账时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime postTime;
	/**
	 * 0:未过账
	 * 1:已过账
	 */
	@ApiModelProperty(value = "0:未过账 1:已过账")
	private Integer postState;


}
