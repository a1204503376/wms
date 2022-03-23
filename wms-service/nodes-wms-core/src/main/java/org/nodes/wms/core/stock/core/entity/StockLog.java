package org.nodes.wms.core.stock.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存日志实体类
 *
 * @author pengwei
 * @since 2020-02-14
 */
@Data
@TableName("wms_stock_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StockLog对象", description = "库存日志")
public class StockLog extends SkuLotBaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 库存日志ID
	 */
	@ApiModelProperty(value = "库存日志ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "wlsl_id", type = IdType.ASSIGN_ID)
	private Long wlslId;
	/**
	 * 库存ID
	 */
	@ApiModelProperty(value = "库存ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
	/**
	 * 库存明细id
	 */
	@ApiModelProperty(value = "库存明细id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockDetailId;
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
	 * 库房名称
	 */
	@ApiModelProperty(value = "库房名称")
	private String whName;
	/**
	 * 库位ID
	 */
	@ApiModelProperty(value = "库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;
	/**
	 * 库位编码
	 */
	@ApiModelProperty(value = "库位编码")
	private String locCode;
	/**
	 * 物品ID
	 */
	@ApiModelProperty(value = "物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
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
	 * 包装ID
	 */
	@ApiModelProperty(value = "包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 包装名称
	 */
	@ApiModelProperty(value = "包装名称")
	private String wspName;
	/**
	 * 层级
	 */
	@ApiModelProperty(value = "层级")
	private Integer skuLevel;
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
	 * 分配数量
	 */
	@ApiModelProperty(value = "分配数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal occupyQty;
	/**
	 * 待上架数量
	 */
	@ApiModelProperty(value = "待上架数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal onShelfQty;
	/**
	 * 下架数量
	 */
	@ApiModelProperty(value = "下架数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal pickQty;
	/**
	 * 盘点占用数量
	 */
	@ApiModelProperty(value = "盘点占用数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal countOccupyQty;
	/**
	 * 容器编码
	 */
	@ApiModelProperty(value = "容器编码")
	private String lpnCode;
	/**
	 * 批次号
	 */
	@ApiModelProperty(value = "批次号")
	private String lotNumber;
	/**
	 * 处理类型（0:新增 1:更新 2:删除）
	 */
	@ApiModelProperty(value = "处理类型（0:新增 1:更新 2:删除）")
	private Integer proType;
	/**
	 * 系统日志ID
	 */
	@ApiModelProperty(value = "系统日志ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long systemProcId;
	/**
	 * 单据明细ID
	 */
	@ApiModelProperty(value = "单据明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long billDetailId;
	/**
	 * 操作时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "操作时间")
	private Date procTime;

	/**
	 * 货主ID
	 */
	@ApiModelProperty(value = "货主ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;

	/**
	 * 箱号
	 */
	@ApiModelProperty("箱号")
	private String boxCode;

	/**
	 * 订单ID
	 */
	@ApiModelProperty("订单ID")
	private Long soBillId;

	/**
	 * 波次ID
	 */
	@ApiModelProperty("波次ID")
	private Long wellenId;

	/**
	 * 库存状态
	 */
	@ApiModelProperty("库存状态")
	private Integer stockStatus;


}
