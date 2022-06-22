
package org.nodes.wms.dao.stock.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 库存实体类
 *
 * @author pengwei
 * @since 2019-12-24
 */
@Data
@TableName("wms_stock")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WmsStock对象", description = "库存")
public class Stock extends SkuLotBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String STOCK_STATUS = "stock_status";

	/**
	 * 库存ID
	 */
	@ApiModelProperty(value = "库存ID")
	@TableId(value = "stock_id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;
	/**
	 * 库区ID
	 */
	@ApiModelProperty(value = "库区ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long zoneId;
	/**
	 * 库区名称
	 */
	@ApiModelProperty("库区名称")
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
	@ApiModelProperty("库位编码")
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
	@ApiModelProperty("物品编码")
	private String skuCode;
	/**
	 * 物品名称
	 */
	@ApiModelProperty("物品名称")
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
	@ApiModelProperty("包装名称")
	private String wspName;
	/**
	 * 层级
	 */
	@ApiModelProperty(value = "层级")
	private Integer skuLevel;
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
//	@ApiModelProperty(value = "待上架数量")
//	@JsonSerialize(using = BigDecimalSerializer.class)
//	private BigDecimal onShelfQty;
	/**
	 * 下架数量
	 */
	@ApiModelProperty(value = "下架数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal pickQty;
	/**
	 * 盘点占用数量
	 */
	@TableField(exist = false)
	@ApiModelProperty(value = "盘点占用数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal countOccupyQty;
	/**
	 * 库存状态
	 */
	@ApiModelProperty(value = "库存状态")
	private Integer stockStatus;
	/**
	 * 最近入库时间(库存数量增加时更新)
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "最近入库时间(库存数量增加时更新)")
	private LocalDateTime lastInTime;
	/**
	 * 最近出库时间(下架数量增加时更新)
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "最近出库时间(下架数量增加时更新)")
	private LocalDateTime lastOutTime;
	/**
	 * 批次号
	 */
	@ApiModelProperty(value = "批次号")
	private String lotNumber;

	/**
	 * 货主ID
	 */
	@ApiModelProperty(value = "货主ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;

	/**
	 * 有效截止日期
	 */
	@ApiModelProperty(value = "有效截止日期")
	private String validTime;
}
