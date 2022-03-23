package org.nodes.wms.core.outstock.so.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.nodes.wms.core.outstock.so.entity.PickPlan;

import java.math.BigDecimal;

/**
 * @author pengwei
 * @program WmsCore
 * @description 生成拣货计划明细VO对象实体类
 * @since 2020-11-04
 */
@Data
public class CreatePickPlanDetailVO extends PickPlan {

	/**
	 * 订单ID
	 */
	@ApiModelProperty("订单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;

	/**
	 * 订单编码
	 */
	@ApiModelProperty("订单编码")
	private String soBillNo;

	/**
	 * 订单明细ID
	 */
	@ApiModelProperty("订单明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soDetailId;

	/**
	 * 物品编码
	 */
	@ApiModelProperty("物品编码")
	private String skuCode;

	/**
	 * 物品名称
	 */
	@ApiModelProperty("物品名称")
	private String skuName;

	/**
	 * 计划量
	 */
	@ApiModelProperty("计划量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal planQty;

	/**
	 * 缺货量
	 */
	@ApiModelProperty("缺货量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal errorQty;

	/**
	 * 计量单位
	 */
	@ApiModelProperty("计量单位")
	private String wsuName;

	/**
	 * 库存ID
	 */
	@ApiModelProperty("库存ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;

	/**
	 * 可用库存量
	 */
	@ApiModelProperty("可用库存量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal stockQty;

	/**
	 * 分配占用量
	 */
	@ApiModelProperty("分配占用量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal occupyQty;

	/**
	 * 盘点占用量
	 */
	@ApiModelProperty("盘点占用量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal countOccupyQty;

	/**
	 * 分配策略ID
	 */
	@ApiModelProperty("分配策略ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long ssoId;

	/**
	 * 分配策略编码
	 */
	@ApiModelProperty("分配策略编码")
	private String ssoCode;

	/**
	 * 分配策略名称
	 */
	@ApiModelProperty("分配策略名称")
	private String ssoName;

	/**
	 * 分配策略处理顺序
	 */
	@ApiModelProperty("分配策略处理顺序")
	private Integer ssodProcOrder;

	/**
	 * 分配计算代码
	 */
	@ApiModelProperty("分配计算代码")
	private Integer outstockFunction;

	/**
	 * 分配计算代码描述
	 */
	@ApiModelProperty("分配计算代码描述")
	private String outstockFunctionDesc;

	/**
	 * 货源库区名称
	 */
	@ApiModelProperty("货源库区名称")
	private String sourceZoneName;

	/**
	 * 明细批次号
	 */
	@ApiModelProperty("明细批次号")
	private String detailLotNumber;

	/**
	 * 明细序列号
	 */
	@ApiModelProperty("明细序列号")
	private String detailSerialNumber;

	/**
	 * 明细批属性指定
	 */
	@ApiModelProperty("明细批属性指定")
	private String detailSkuLot;

	/**
	 * 策略批属性指定
	 */
	@ApiModelProperty("策略批属性指定")
	private String ssoSkuLot;

	/**
	 * 周转方式
	 */
	@ApiModelProperty("周转方式")
	private String turnoverType;

	/**
	 * 异常消息
	 */
	@ApiModelProperty("异常消息")
	private String exception;

	/**
	 * 操作用户ID
	 */
	@ApiModelProperty("操作用户ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;
}
