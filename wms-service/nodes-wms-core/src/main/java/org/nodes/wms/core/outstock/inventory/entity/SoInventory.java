package org.nodes.wms.core.outstock.inventory.entity;

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
 * 发货清单主表
 * 实体类
 *
 * @author NodeX
 * @since 2021-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SoInventory对象", description = "发货清单主表")
public class SoInventory extends AttributeBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键ID")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
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
	private String soBillNo;
	/**
	 * 单据状态
	 */
	@ApiModelProperty(value = "单据状态")
	private Integer soBillState;
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
	 * 上位系统单据编号
	 */
	@ApiModelProperty(value = "上位系统单据编号")
	private String orderNo;
	/**
	 * 上位系统备注
	 */
	@ApiModelProperty(value = "上位系统备注")
	private String soRemark;
	/**
	 * 上位系统最后更新时间
	 */
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "上位系统最后更新时间")
	private LocalDateTime lastUpdateDate;
	/**
	 * 上位系统订单创建时间
	 */
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "上位系统订单创建时间")
	private LocalDateTime preCreateDate;
	/**
	 * 预计发货日期
	 */
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "预计发货日期")
	private LocalDateTime scheduledOutstockTime;
	/**
	 * 单据完成时间
	 */
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "单据完成时间")
	private LocalDateTime finishDate;
	/**
	 * 发货方式
	 */
	@ApiModelProperty(value = "发货方式")
	private Integer transportCode;
	/**
	 * 出库方式
	 */
	@ApiModelProperty(value = "出库方式")
	private Integer outstockType;
	/**
	 * 客户编号
	 */
	@ApiModelProperty(value = "客户编号")
	@JsonProperty("cCode")
	private String cCode;
	/**
	 * 客户名称
	 */
	@JsonProperty("cName")
	@ApiModelProperty(value = "客户名称")
	private String cName;
	/**
	 * 收货人地址
	 */
	@ApiModelProperty(value = "收货人地址")
	private String address;
	/**
	 * 收获联系人
	 */
	@ApiModelProperty(value = "收获联系人")
	private String contact;
	/**
	 * 收货人联系电话
	 */
	@ApiModelProperty(value = "收货人联系电话")
	private String phone;
	/**
	 * 客户地址经度
	 */
	@ApiModelProperty(value = "客户地址经度")
	private BigDecimal cXCoor;
	/**
	 * 客户地址纬度
	 */
	@ApiModelProperty(value = "客户地址纬度")
	private BigDecimal cYCoor;
	/**
	 * 物流编码
	 */
	@ApiModelProperty(value = "物流编码")
	private String expressCode;
	/**
	 * 物流名称
	 */
	@ApiModelProperty(value = "物流名称")
	private String expressName;
	/**
	 * 物流电话
	 */
	@ApiModelProperty(value = "物流电话")
	private String expressPhone;
	/**
	 * 物流地址
	 */
	@ApiModelProperty(value = "物流地址")
	private String expressAddress;
	/**
	 * 物流地址经度
	 */
	@ApiModelProperty(value = "物流地址经度")
	private BigDecimal expressX;
	/**
	 * 物流地址纬度
	 */
	@ApiModelProperty(value = "物流地址纬度")
	private BigDecimal expressY;
	/**
	 * 发货单备注
	 */
	@ApiModelProperty(value = "发货单备注")
	private String soBillRemark;
	/**
	 * 发货完成时间
	 */
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "发货完成时间")
	private LocalDateTime transportDate;
	/**
	 * 单据创建人员名称
	 */
	@ApiModelProperty(value = "单据创建人员名称")
	private String billCreator;
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
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "过账时间")
	private LocalDateTime postTime;
	/**
	 * 0:未过账
	 * 1:已过账
	 */
	@ApiModelProperty(value = "0:未过账1:已过账")
	private Integer postState;
	/**
	 * 10：未发运20：发运中30：已发运
	 */
	@ApiModelProperty(value = "10：未发运20：发运中30：已发运")
	private Integer shipState;
}
