package org.nodes.wms.dao.common.stock;

import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;

import java.math.BigDecimal;
import java.util.List;

public class StockUtil {

	/**
	 * 计算库存余额 = stockQty - pickQty
	 *
	 * @param sotck
	 * @return 余额
	 */
	public static BigDecimal getStockBalance(Stock sotck) {
		BigDecimal qty = sotck.getStockQty().subtract(sotck.getPickQty());
		if (BigDecimalUtil.ge(BigDecimal.ZERO, qty)) {
			throw new ServiceException("上架数量小于下架数量");
		}
		return qty;
	}

	/**
	 * 计算库存余额 = stockQty - pickQty。计算库存量的时候需要考虑计量单位
	 * 如果计算的包装层级不一致则抛异常
	 *
	 * @param stockList
	 * @return 余额
	 */
	public static BigDecimal getStockBalance(List<Stock> stockList) {
		BigDecimal qtySubtractMax = BigDecimal.ZERO;
		for (Stock stock : stockList
		) {
			if (!Func.equals(stockList.get(0).getSkuLevel(), stock.getSkuLevel())) {
				throw new ServiceException("箱码包装层级不一致");
			}
			BigDecimal lowDecimal = stock.getStockQty().subtract(stock.getPickQty());
			qtySubtractMax = qtySubtractMax.add(lowDecimal);
		}

		if (BigDecimalUtil.ge(BigDecimal.ZERO, qtySubtractMax)) {
			throw new ServiceException("上架数量小于下架数量");
		}
		return qtySubtractMax;
	}

	/**
	 * 计算库存的可用量 = stockQty + pickQty - occupyQty
	 *
	 * @param stock
	 * @return 可用量
	 */
	public static BigDecimal getStockEnable(Stock stock) {
		BigDecimal qty = stock.getStockQty().add(stock.getPickQty()).subtract(stock.getOccupyQty());
		if (BigDecimalUtil.ge(BigDecimal.ZERO, qty)) {
			throw new ServiceException("可用量不足，请及时补充库存");
		}
		return qty;
	}

	/**
	 * 计算库存的可用量 = stockQty + pickQty - occupyQty。计算库存量的时候需要考虑计量单位
	 * 如果计算的包装层级不一致则抛异常
	 *
	 * @param stockList
	 * @return 可用量
	 */
	public static BigDecimal getStockEnable(List<Stock> stockList) {
		BigDecimal qtySubtractMax = BigDecimal.ZERO;
		for (Stock stock : stockList
		) {
			if (!Func.equals(stockList.get(0).getSkuLevel(), stock.getSkuLevel())) {
				throw new ServiceException("箱码包装层级不一致");
			}
			BigDecimal lowDecimal = stock.getStockQty().add(stock.getPickQty()).subtract(stock.getOccupyQty());
			qtySubtractMax = qtySubtractMax.add(lowDecimal);
		}

		if (BigDecimalUtil.ge(BigDecimal.ZERO, qtySubtractMax)) {
			throw new ServiceException("上架数量小于下架数量");
		}
		return qtySubtractMax;
	}
}
