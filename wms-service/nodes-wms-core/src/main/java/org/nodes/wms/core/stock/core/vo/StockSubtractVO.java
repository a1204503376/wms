package org.nodes.wms.core.stock.core.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.dao.stock.entities.Stock;

/**
 * author: pengwei
 * date: 2021/4/16 11:41
 * description: StockSubtractVO
 */
@Data
public class StockSubtractVO {
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
