package org.nodes.wms.core.stock.core.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.stock.core.entity.Stock;

/**
 * @author pengwei
 * @program WmsCore
 * @description 库存扣减返回值
 * @create 20200225
 */
@Data
public class StockSubtractRltDTO {

	/**
	 * 订单ID
	 */
	@ApiModelProperty("订单ID")
	private Long soBillId;

	/**
	 * 订单明细ID
	 */
	@ApiModelProperty("订单明细ID")
	private Long soDetailId;

	/**
	 * 扣减的库存对象
	 */
	@ApiModelProperty("扣减的库存对象")
	private Stock stock;
}
