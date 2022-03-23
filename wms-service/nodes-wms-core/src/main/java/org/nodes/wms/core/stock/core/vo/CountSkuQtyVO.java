
package org.nodes.wms.core.stock.core.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import java.math.BigDecimal;

/**
 * 盘点单明细物料库存实体类
 */
@Data
@ApiModel(value = "CountSkuQtyVO对象", description = "盘点单明细物料库存")
public class CountSkuQtyVO extends SkuLotBaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 库存
	 */
	@ApiModelProperty(value = "库存")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal qty;
	/**
	 * 物料编号
	 */
	@ApiModelProperty(value = "物料编号")
	private String skuCode;

	/**
	 * 物料id
	 */
	@ApiModelProperty(value = "物料id")
	private Long skuId;

	/**
	 * 盘点模式
	 * 0：盲盘 1：明盘
	 */
	@ApiModelProperty(value = "盘点模式")
	private String mode;
}
