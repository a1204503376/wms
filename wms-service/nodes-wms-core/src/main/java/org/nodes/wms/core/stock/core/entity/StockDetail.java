package org.nodes.wms.core.stock.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.ToString;
import org.apache.ibatis.type.LongTypeHandler;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.springblade.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.core.tenant.mp.TenantEntity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存明细表实体类
 *
 * @author NodeX
 * @since 2021-10-12
 */
@Data
@TableName("wms_stock_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StockDetail对象", description = "库存明细表")
public class StockDetail extends TenantEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键id")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	/**
	 * 库存id
	 */
	@ApiModelProperty(value = "库存id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
	/**
	 * 库房id
	 */
	@ApiModelProperty(value = "库房id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 库区id
	 */
	@ApiModelProperty(value = "库区id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long zoneId;
	/**
	 * 库位id
	 */
	@ApiModelProperty("库位id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;
	/**
	 * 物品id
	 */
	@ApiModelProperty("物品id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
	/**
	 * 包装id
	 */
	@ApiModelProperty("包装id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 容器编码
	 */
	@ApiModelProperty(value = "容器编码")
	private String lpnCode;
	/**
	 * 箱码
	 */
	@ApiModelProperty(value = "箱码")
	private String boxCode;
	/**
	 * 未接收数量
	 */
	@ApiModelProperty(value = "未接收数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal unreceivedQty;
	/**
	 * 库存数量
	 */
	@ApiModelProperty(value = "库存数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal stockQty;
	/**
	 * 下架数量
	 */
	@ApiModelProperty(value = "下架数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal pickQty;
	/**
	 * 批次号
	 */
	@ApiModelProperty("批次号")
	private String lotNumber;
	/**
	 * 订单明细id
	 */
	@ApiModelProperty("订单明细id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long billDetailId;
	/**
	 * 出库单id
	 */
	@ApiModelProperty("出库单id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
	/**
	 * 波次id
	 */
	@ApiModelProperty("波次id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wellenId;
	/**
	 * 库存状态
	 */
	@ApiModelProperty("库存状态")
	private Integer stockStatus;
}
