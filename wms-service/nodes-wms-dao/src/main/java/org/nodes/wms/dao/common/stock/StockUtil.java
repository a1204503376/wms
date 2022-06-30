package org.nodes.wms.dao.common.stock;

import org.nodes.wms.dao.stock.entities.Stock;

import java.math.BigDecimal;
import java.util.List;

public class StockUtil {

	/**
	 * 计算库存余额 = stockQty - pickQty
	 * @param sotck
	 * @return 余额
	 */
	public static BigDecimal getStockBalance(Stock sotck){
		return BigDecimal.ZERO;
	}

	/**
	 * 计算库存余额 = stockQty - pickQty。计算库存量的时候需要考虑计量单位
	 * 如果计算的包装层级不一致则抛异常
	 * @param stockList
	 * @return
	 */
	public static BigDecimal getStockBalance(List<Stock> stockList){
		return BigDecimal.ZERO;
	}

	/**
	 * 计算库存的可用量 = stockQty + pickQty - occupyQty
	 * @param stock
	 * @return
	 */
	public static BigDecimal getStockEnable(Stock stock){
		return BigDecimal.ZERO;
	}

	/**
	 * 计算库存的可用量 = stockQty + pickQty - occupyQty。计算库存量的时候需要考虑计量单位
	 * 如果计算的包装层级不一致则抛异常
	 * @param stockList
	 * @return
	 */
	public static BigDecimal getStockEnable(List<Stock> stockList){
		return BigDecimal.ZERO;
	}
}
