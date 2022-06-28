package org.nodes.wms.dao.stock;

import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 库存Dao接口
 **/
public interface StockDao {

	/**
	 * 根据库位id查询库存数量
	 *
	 * @param locIdList: 库位id集合
	 * @return Map<String, Object>
	 */
	Map<String, Object> getStockQtyByLocIdList(List<Long> locIdList);

	/**
	 * 根据库位id获取库存物品总数
	 *
	 * @param locIdList:
	 * @return Integer
	 */
	Integer getStockSkuCountByLocIdList(List<Long> locIdList);

	/**
	 * 获取所有库存数据，含冻结的库存。如果出库暂存区为空则表示查询的库存数据包含出库暂存区的
	 *
	 * @param boxCode
	 * @param pickToLocIdList
	 * @return
	 */
	List<Stock> getStockByBoxCode(String boxCode, List<Long> pickToLocIdList);

	/**
	 * 获取所有库存数据，含冻结的库存。如果出库暂存区为空则表示查询的库存数据包含出库暂存区的
	 *
	 * @param lpnCode
	 * @param pickToLocIdList
	 * @return
	 */
	List<Stock> getStockByLpnCode(String lpnCode, List<Long> pickToLocIdList);

	List<Stock> getStock(StockStatusEnum status, Long woId, Long locId,
						 Long skuId, String boxCode, String lpnCode);

	List<Stock> getStockByLocId(Long locId);

	Stock saveNewStock(Stock stock);

	Stock updateStock(Stock stock);

	Stock updateStock(Long stockId, BigDecimal stockQty, BigDecimal stayStockQty,
				 BigDecimal pickQty, LocalDateTime lastInTime, LocalDateTime lastOutTime);
}
