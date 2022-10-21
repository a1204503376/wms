package org.nodes.wms.dao.stock.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class StockAgvAndPickResponse implements Serializable {
	private static final long serialVersionUID = -7968810851634369230L;
	/**
	 * 人工区库存
	 */
	private BigDecimal pickStockBalance;
	/**
	 * 自动区库存
	 */
	private BigDecimal agvStockBalance;
}
