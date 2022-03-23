package org.nodes.wms.core.stock.core.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
 * 库存快照实体类
 *
 * @author NodeX
 * @since 2021-05-28
 */
@Data
@TableName("wms_stock_snapshoot")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StockSnapshoot对象", description = "库存快照")
public class StockSnapshoot extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 库存快照ID
	 */
	@ApiModelProperty(value = "ID")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 快照日期
	 */
	@ApiModelProperty(value = "快照日期")
	private String snapshootDate;
	/**
	 * 库存ID
	 */
	@ApiModelProperty(value = "库存ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
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
	 * 库区ID
	 */
	@ApiModelProperty(value = "库区ID")
	private Long zoneId;
	/**
	 * 库区编码
	 */
	@ApiModelProperty(value = "库区编码")
	private String zoneCode;
	/**
	 * 库区名称
	 */
	@ApiModelProperty(value = "库区名称")
	private String zoneName;
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
	 * 物品包装层级
	 */
	@ApiModelProperty(value = "物品包装层级")
	private Integer skuLevel;
	/**
	 * 库存数量
	 */
	@ApiModelProperty(value = "库存数量")
	private BigDecimal stockQty;
	/**
	 * 分配占用数量
	 */
	@ApiModelProperty(value = "分配占用数量")
	private BigDecimal occupyQty;
	/**
	 * 待上架数量
	 */
	@ApiModelProperty(value = "待上架数量")
	private BigDecimal onShelfQty;
	/**
	 * 下架数量
	 */
	@ApiModelProperty(value = "下架数量")
	private BigDecimal pickQty;
	/**
	 * 盘点占用数量
	 */
	@ApiModelProperty(value = "盘点占用数量")
	private BigDecimal countOccupyQty;
	/**
	 * 结余数量
	 */
	@ApiModelProperty(value = "结余数量")
	private BigDecimal qty;
	/**
	 * 对比上一天差异数量
	 */
	@ApiModelProperty(value = "对比上一天差异数量")
	private BigDecimal diffQty;
	/**
	 * 容器编码
	 */
	@ApiModelProperty(value = "容器编码")
	private String lpnCode;
	/**
	 * 箱号
	 */
	@ApiModelProperty(value = "箱号")
	private String boxCode;
	/**
	 * 批次号
	 */
	@ApiModelProperty(value = "批次号")
	private String lotNumber;
	/**
	 * 订单明细ID
	 */
	@ApiModelProperty(value = "订单明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long billDetailId;
	/**
	 * 出库单ID
	 */
	@ApiModelProperty(value = "出库单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
	/**
	 * 波次ID
	 */
	@ApiModelProperty(value = "波次ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wellenId;
	/**
	 * 库存状态
	 */
	@ApiModelProperty(value = "库存状态")
	private Integer stockStatus;
	/**
	 * 上一次入库时间
	 */
	@ApiModelProperty(value = "上一次入库时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime lastInTime;
	/**
	 * 上一次出库时间
	 */
	@ApiModelProperty(value = "上一次出库时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime lastOutTime;
	/**
	 * 批属性1
	 */
	@ApiModelProperty(value = "批属性1")
	private String skuLot1;
	/**
	 * 批属性2
	 */
	@ApiModelProperty(value = "批属性2")
	private String skuLot2;
	/**
	 * 批属性3
	 */
	@ApiModelProperty(value = "批属性3")
	private String skuLot3;
	/**
	 * 批属性4
	 */
	@ApiModelProperty(value = "批属性4")
	private String skuLot4;
	/**
	 * 批属性5
	 */
	@ApiModelProperty(value = "批属性5")
	private String skuLot5;
	/**
	 * 批属性6
	 */
	@ApiModelProperty(value = "批属性6")
	private String skuLot6;
	/**
	 * 批属性7
	 */
	@ApiModelProperty(value = "批属性7")
	private String skuLot7;
	/**
	 * 批属性8
	 */
	@ApiModelProperty(value = "批属性8")
	private String skuLot8;
	/**
	 * 批属性9
	 */
	@ApiModelProperty(value = "批属性9")
	private String skuLot9;
	/**
	 * 批属性10
	 */
	@ApiModelProperty(value = "批属性10")
	private String skuLot10;
	/**
	 * 批属性11
	 */
	@ApiModelProperty(value = "批属性11")
	private String skuLot11;
	/**
	 * 批属性12
	 */
	@ApiModelProperty(value = "批属性12")
	private String skuLot12;
	/**
	 * 批属性13
	 */
	@ApiModelProperty(value = "批属性13")
	private String skuLot13;
	/**
	 * 批属性14
	 */
	@ApiModelProperty(value = "批属性14")
	private String skuLot14;
	/**
	 * 批属性15
	 */
	@ApiModelProperty(value = "批属性15")
	private String skuLot15;
	/**
	 * 批属性16
	 */
	@ApiModelProperty(value = "批属性16")
	private String skuLot16;
	/**
	 * 批属性17
	 */
	@ApiModelProperty(value = "批属性17")
	private String skuLot17;
	/**
	 * 批属性18
	 */
	@ApiModelProperty(value = "批属性18")
	private String skuLot18;
	/**
	 * 批属性19
	 */
	@ApiModelProperty(value = "批属性19")
	private String skuLot19;
	/**
	 * 批属性20
	 */
	@ApiModelProperty(value = "批属性20")
	private String skuLot20;
	/**
	 * 批属性21
	 */
	@ApiModelProperty(value = "批属性21")
	private String skuLot21;
	/**
	 * 批属性22
	 */
	@ApiModelProperty(value = "批属性22")
	private String skuLot22;
	/**
	 * 批属性23
	 */
	@ApiModelProperty(value = "批属性23")
	private String skuLot23;
	/**
	 * 批属性24
	 */
	@ApiModelProperty(value = "批属性24")
	private String skuLot24;
	/**
	 * 批属性25
	 */
	@ApiModelProperty(value = "批属性25")
	private String skuLot25;
	/**
	 * 批属性26
	 */
	@ApiModelProperty(value = "批属性26")
	private String skuLot26;
	/**
	 * 批属性27
	 */
	@ApiModelProperty(value = "批属性27")
	private String skuLot27;
	/**
	 * 批属性28
	 */
	@ApiModelProperty(value = "批属性28")
	private String skuLot28;
	/**
	 * 批属性29
	 */
	@ApiModelProperty(value = "批属性29")
	private String skuLot29;
	/**
	 * 批属性30
	 */
	@ApiModelProperty(value = "批属性30")
	private String skuLot30;
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
	 * 货主名称
	 */
	@ApiModelProperty(value = "货主名称")
	private String ownerName;


}
