package org.nodes.wms.dao.stock.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 分配调整：根据物品id查找物品可分配库存信息
 */
@Data
public class GetStockByDistributeAdjustResponse implements Serializable {
	private static final long serialVersionUID = -7968810851634369230L;
	/**
	 * 可分配物品库存信息
	 */
	List<StockSoPickPlanResponse> stockSoPickPlanList;
	/**
	 * 人工区库存
	 */
	private BigDecimal pickStockBalance;
	/**
	 * 自动区库存
	 */
	private BigDecimal agvStockBalance;
}
