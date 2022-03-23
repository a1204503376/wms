package org.nodes.wms.core.outstock.sales.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
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
 * 发货单明细
 * 实体类
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SalesDetail对象", description = "发货单明细 ")
public class SalesDetail extends AttributeBase implements Serializable {

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
	private BigDecimal planQty;
	/**
	 * 实绩数量
	 */
	@ApiModelProperty(value = "实绩数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal scanQty;
	/**
	 * 剩余数量
	 */
	@ApiModelProperty(value = "剩余数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
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
	 * 物品批属性1
	 */
	@ApiModelProperty(value = "物品批属性1")
	private String skuLot1;
	/**
	 * 物品批属性2
	 */
	@ApiModelProperty(value = "物品批属性2")
	private String skuLot2;
	/**
	 * 物品批属性3
	 */
	@ApiModelProperty(value = "物品批属性3")
	private String skuLot3;
	/**
	 * 物品批属性4
	 */
	@ApiModelProperty(value = "物品批属性4")
	private String skuLot4;
	/**
	 * 物品批属性5
	 */
	@ApiModelProperty(value = "物品批属性5")
	private String skuLot5;
	/**
	 * 物品批属性6
	 */
	@ApiModelProperty(value = "物品批属性6")
	private String skuLot6;
	/**
	 * 物品批属性7
	 */
	@ApiModelProperty(value = "物品批属性7")
	private String skuLot7;
	/**
	 * 物品批属性8
	 */
	@ApiModelProperty(value = "物品批属性8")
	private String skuLot8;
	/**
	 * 物品批属性9
	 */
	@ApiModelProperty(value = "物品批属性9")
	private String skuLot9;
	/**
	 * 物品批属性10
	 */
	@ApiModelProperty(value = "物品批属性10")
	private String skuLot10;
	/**
	 * 物品批属性11
	 */
	@ApiModelProperty(value = "物品批属性11")
	private String skuLot11;
	/**
	 * 物品批属性12
	 */
	@ApiModelProperty(value = "物品批属性12")
	private String skuLot12;
	/**
	 * 物品批属性13
	 */
	@ApiModelProperty(value = "物品批属性13")
	private String skuLot13;
	/**
	 * 物品批属性14
	 */
	@ApiModelProperty(value = "物品批属性14")
	private String skuLot14;
	/**
	 * 物品批属性15
	 */
	@ApiModelProperty(value = "物品批属性15")
	private String skuLot15;
	/**
	 * 物品批属性16
	 */
	@ApiModelProperty(value = "物品批属性16")
	private String skuLot16;
	/**
	 * 物品批属性17
	 */
	@ApiModelProperty(value = "物品批属性17")
	private String skuLot17;
	/**
	 * 物品批属性18
	 */
	@ApiModelProperty(value = "物品批属性18")
	private String skuLot18;
	/**
	 * 物品批属性19
	 */
	@ApiModelProperty(value = "物品批属性19")
	private String skuLot19;
	/**
	 * 物品批属性20
	 */
	@ApiModelProperty(value = "物品批属性20")
	private String skuLot20;
	/**
	 * 物品批属性21
	 */
	@ApiModelProperty(value = "物品批属性21")
	private String skuLot21;
	/**
	 * 物品批属性22
	 */
	@ApiModelProperty(value = "物品批属性22")
	private String skuLot22;
	/**
	 * 物品批属性23
	 */
	@ApiModelProperty(value = "物品批属性23")
	private String skuLot23;
	/**
	 * 物品批属性24
	 */
	@ApiModelProperty(value = "物品批属性24")
	private String skuLot24;
	/**
	 * 物品批属性25
	 */
	@ApiModelProperty(value = "物品批属性25")
	private String skuLot25;
	/**
	 * 物品批属性26
	 */
	@ApiModelProperty(value = "物品批属性26")
	private String skuLot26;
	/**
	 * 物品批属性27
	 */
	@ApiModelProperty(value = "物品批属性27")
	private String skuLot27;
	/**
	 * 物品批属性28
	 */
	@ApiModelProperty(value = "物品批属性28")
	private String skuLot28;
	/**
	 * 物品批属性29
	 */
	@ApiModelProperty(value = "物品批属性29")
	private String skuLot29;
	/**
	 * 物品批属性30
	 */
	@ApiModelProperty(value = "物品批属性30")
	private String skuLot30;
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
	private String billKey;
	/**
	 * 上位系统单编号
	 */
	@ApiModelProperty(value = "上位系统单编号")
	private String orderNo;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

}
