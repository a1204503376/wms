package org.nodes.wms.dao.stock;

import org.nodes.wms.dao.stock.entities.StockBalance;

import java.util.List;

/**
 * 收发存库存Dao接口
 *
 * @author nodesc
 */
public interface StockBalanceDao {

	/**
	 * 根据时间范围获取收发存库存集合
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 收发存库存集合
	 */
	List<StockBalance> getStockBalanceList(String startTime, String endTime);

	/**
	 * 批量保存收发存库存实体
	 *
	 * @param stockBalanceList 收发存库存集合
	 */
	void savaStockBalanceBatch(List<StockBalance> stockBalanceList);
}
