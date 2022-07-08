package org.nodes.wms.core.instock.purchase.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.nodes.wms.core.common.entity.AttributeBase;

import java.io.Serializable;

/**
 * 收货单明细表实体类
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Data
@ApiModel(value = "PoDetail对象", description = "收货单明细表")
public class PoDetail extends AttributeBase implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 收货单明细ID
	 */
	@ApiModelProperty(value = "收货单明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "po_detail_id", type= IdType.ASSIGN_ID)
	private Long poDetailId;
	/**
	 * 收货单ID
	 */
	@ApiModelProperty(value = "收货单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long poBillId;
	/**
	 * 单据编码
	 */
	@ApiModelProperty(value = "单据编码")
	private String poBillNo;
	/**
	 * 订单行号
	 */
	@ApiModelProperty(value = "订单行号")
	private String poLineNo;
	/**
	 * 上位系统单据明细唯一标识
	 */
	@ApiModelProperty(value = "上位系统单据明细唯一标识")
	private String poBillDetailKey;
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
	 * 基础计量单位编码
	 */
	@ApiModelProperty(value = "基础计量单位编码")
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
	private BigDecimal planQty;
	/**
	 * 实绩数量
	 */
	@ApiModelProperty(value = "实绩数量")
	private BigDecimal scanQty;
	/**
	 * 剩余数量
	 */
	@ApiModelProperty(value = "剩余数量")
	private BigDecimal surplusQty;
	/**
	 * 收货库房
	 */
	@ApiModelProperty(value = "收货库房")
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
	private String poDetailRemark;
	/**
	 * 接收状态
	 */
	@ApiModelProperty(value = "接收状态 ")
	private Integer detailStatus;
	/**
	 * 序列号导入状态  Y:已导入N：未导入
	 */
	@ApiModelProperty(value = "序列号导入状态  Y:已导入N：未导入")
	private String importSn;
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
	 * 差异报告Id
	 */
	@ApiModelProperty(value = "差异报告Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wcrepId;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

}
