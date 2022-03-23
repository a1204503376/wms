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

import java.math.BigDecimal;

/**
 * @author pengwei
 * @program WmsCore
 * @description ERP库存实体
 * @create 20200331
 */
@Data
@TableName("erp_stock")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ErpStock对象", description = "ERP库存")
public class ErpStock extends BaseEntity {

	/**
	 * 库存ID
	 */
	@ApiModelProperty(value = "库存ID")
	@TableId(value = "stock_id", type = IdType.ID_WORKER)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;

	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	@ApiModelProperty(value = "物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

	@ApiModelProperty(value = "包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;

	@ApiModelProperty(value = "物品包装层级")
	private Integer skuLevel;

	@ApiModelProperty(value = "货主ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;

	@ApiModelProperty(value = "库存量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal stockQty;
}

