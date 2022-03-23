package org.nodes.wms.core.stock.core.entity;

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
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 尾箱打包日志表实体类
 *
 * @author NodeX
 * @since 2020-07-13
 */
@Data
@TableName("log_stock_pack")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StockPack对象", description = "尾箱打包日志表")
public class LogStockPack extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 尾箱打包日志ID
	 */
	@ApiModelProperty(value = "尾箱打包日志ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "lsp_id", type = IdType.ASSIGN_ID)
	private Long lspId;

	/**
	 * 尾箱库存ID
	 */
	@ApiModelProperty(value = "库存ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;

	/**
	 * 尾箱打包ID
	 */
	@ApiModelProperty(value = "尾箱打包ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wlspId;
	/**
	 * 分组ID
	 */
	@ApiModelProperty(value = "分组ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long groupId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 库区ID
	 */
	@ApiModelProperty(value = "库区ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long zoneId;
	/**
	 * 库位ID
	 */
	@ApiModelProperty(value = "库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;
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
	 * 包装层级
	 */
	@ApiModelProperty(value = "包装层级")
	private Integer skuLevel;
	/**
	 * 库存容器编码
	 */
	@ApiModelProperty(value = "库存容器编码")
	private String stockLpnCode;
	/**
	 * 库存箱号
	 */
	@ApiModelProperty(value = "库存箱号")
	private String stockBoxCode;
	/**
	 * 操作类型
	 */
	@ApiModelProperty(value = "操作类型")
	private Integer procType;
	/**
	 * 库存批次号
	 */
	@ApiModelProperty(value = "库存批次号")
	private String stockLot;
	/**
	 * 数量
	 */
	@ApiModelProperty(value = "数量")
	private BigDecimal qty;
	/**
	 * 操作数量
	 */
	@ApiModelProperty(value = "操作数量")
	private BigDecimal procQty;
	/**
	 * 新箱号
	 */
	@ApiModelProperty(value = "新箱号")
	private String boxCode;
	/**
	 * 操作时间
	 */
	@ApiModelProperty(value = "操作时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime procTime;
	/**
	 * 操作人员
	 */
	@ApiModelProperty(value = "操作人员")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long procUser;
	/**
	 * 系统日志ID
	 */
	@ApiModelProperty(value = "系统日志ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long systemProcId;


}
