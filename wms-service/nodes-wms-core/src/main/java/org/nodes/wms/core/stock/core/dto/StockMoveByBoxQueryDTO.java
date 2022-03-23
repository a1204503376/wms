package org.nodes.wms.core.stock.core.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.entity.SkuLotBaseEntity;

import java.math.BigDecimal;

/**
 * 按箱移动数据传输类
 *
 * @author pengwei
 * @since 2020-07-20
 */
@Data
public class StockMoveByBoxQueryDTO {

	/**
	 * 物品名称
	 */
	@ApiModelProperty("物品ID")
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
	 * 移动数量
	 */
	@ApiModelProperty("移动数量")
	@JsonSerialize(using = ToStringSerializer.class)
	private BigDecimal moveQty;

	/**
	 * 单位名称
	 */
	@ApiModelProperty("单位名称")
	private String wsuName;

	/**
	 * 批属性
	 */
	@ApiModelProperty("批属性")
	private SkuLotBaseEntity skuLots;

	/**
	 * 源库位编码
	 */
	@ApiModelProperty("源库位编码")
	private String sourceLocCode;
}
