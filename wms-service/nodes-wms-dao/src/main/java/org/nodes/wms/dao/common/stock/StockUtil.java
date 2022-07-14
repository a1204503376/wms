package org.nodes.wms.dao.common.stock;

import org.apache.commons.lang.NullArgumentException;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class StockUtil {

	/**
	 * 计算库存余额 = stockQty - pickQty
	 *
	 * @param stock 库存对象
	 * @return 余额
	 */
	public static BigDecimal getStockBalance(Stock stock) {
		BigDecimal qty = stock.getStockQty().subtract(stock.getPickQty());
		if (BigDecimalUtil.ge(BigDecimal.ZERO, qty)) {
			throw new ServiceException("上架数量小于下架数量");
		}
		return qty;
	}

	/**
	 * 计算库存余额 = stockQty - pickQty。计算库存量的时候需要考虑计量单位
	 * 如果计算的包装层级不一致则抛异常
	 *
	 * @param stockList 库存对象集合
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
	 * @param stock 库存对象
	 * @return 可用量
	 */
	public static BigDecimal getStockEnable(Stock stock) {
		BigDecimal qty = stock.getStockQty().add(stock.getPickQty()).subtract(stock.getOccupyQty());
		if (BigDecimalUtil.ge(BigDecimal.ZERO, qty)) {
			throw new ServiceException("计算库存可用量失败，可用量小于0");
		}
		return qty;
	}

	/**
	 * 计算库存的可用量 = stockQty + pickQty - occupyQty。计算库存量的时候需要考虑计量单位
	 * 如果计算的包装层级不一致则抛异常
	 *
	 * @param stockList 库存对象集合
	 * @return 可用量
	 */
	public static BigDecimal getStockEnable(List<Stock> stockList) {
		BigDecimal qtySubtractMax = BigDecimal.ZERO;
		for (Stock stock : stockList) {
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

	public static void resetStockInfo(Stock stock) {
		if (Func.isNull(stock)) {
			throw new NullArgumentException("重置库存信息失败，object is null");
		}

		stock.setStockId(null);
		stock.setStockQty(BigDecimal.ZERO);
		stock.setPickQty(BigDecimal.ZERO);
		stock.setStayStockQty(BigDecimal.ZERO);
		stock.setOccupyQty(BigDecimal.ZERO);
		stock.setLpnCode(null);
		stock.setBoxCode(null);
		stock.setLocId(null);
		stock.setLocCode(null);
		stock.setZoneId(null);
		stock.setZoneCode(null);
		stock.setLastOutTime(null);
		stock.setLastInTime(null);
		stock.setVersion(null);
	}

	/**
	 * 验证库存是否有足够数量来下架,如果没有则抛异常
	 *
	 * @param sourceStock 要下架的库存
	 * @param pickQty     下架数量
	 * @param reason      失败场景
	 */
	public static void assertPick(Stock sourceStock, BigDecimal pickQty, String reason) {
		BigDecimal enableQty = getStockEnable(sourceStock);
		if (BigDecimalUtil.lt(enableQty, pickQty)) {
			if (Func.isEmpty(reason)) {
				reason = "判断库存下架量要大于可用量";
			}

			throw new ServiceException(String.format("%s失败,stockId:%d,enable:%f,pickQty:%f",
				reason, sourceStock.getStockId(), enableQty, pickQty));
		}
	}

	/**
	 * 库存下架, 如果下架量超过可用量则抛异常
	 *
	 * @param stock   库存对象
	 * @param pickQty 下架量
	 * @param reason  操作的场景
	 */
	public static void pickQty(Stock stock, BigDecimal pickQty, String reason) {
		assertPick(stock, pickQty, reason);

		stock.setPickQty(stock.getPickQty().add(pickQty));
		stock.setLastOutTime(LocalDateTime.now());
	}

	/**
	 * 上架库存
	 *
	 * @param stock  库存对象
	 * @param addQty 上架量
	 */
	public static void addQty(Stock stock, BigDecimal addQty) {
		stock.setStockQty(stock.getStockQty().add(addQty));
		stock.setLastInTime(LocalDateTime.now());
	}
}
