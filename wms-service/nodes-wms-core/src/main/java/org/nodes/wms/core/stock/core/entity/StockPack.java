package org.nodes.wms.core.stock.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.springblade.core.mp.base.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 尾箱打包实体类
 *
 * @author pengwei
 * @since 2020-07-07
 */
@Data
@TableName("wms_stock_pack")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StockPack对象", description = "尾箱打包")
public class StockPack extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 尾箱打包ID
	 */
	@ApiModelProperty(value = "尾箱打包ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "wlsp_id", type = IdType.ASSIGN_ID)
	private Long wlspId;
	/**
	 * 分组ID
	 */
	@ApiModelProperty(value = "分组ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long groupId;
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
	 * 库存批次号
	 */
	@ApiModelProperty(value = "库存批次号")
	private String stockLot;

	/**
	 * 打包状态（0：已下发， 10：待打包， 20：打包完成）
	 */
	@ApiModelProperty(value = "打包状态（0：已下发， 10：待打包， 20：打包完成）")
	private Integer packState;
	/**
	 * 库存数量
	 */
	@ApiModelProperty(value = "库存数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal stockQty;
	/**
	 * 拣货数量
	 */
	@ApiModelProperty(value = "拣货数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal pickQty;
	/**
	 * 打包数量
	 */
	@ApiModelProperty(value = "打包数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal packQty;


}
