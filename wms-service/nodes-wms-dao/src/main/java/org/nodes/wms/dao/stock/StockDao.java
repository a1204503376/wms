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
	 * @param excludeLocIdList
	 * @return
	 */
	List<Stock> getStockByBoxCodeExcludeLoc(String boxCode, List<Long> excludeLocIdList);

	/**
	 * 根据boxCode获取指定库存
	 * @param boxCode 必填，不能为空
	 * @param locIdList 可为空
	 * @return
	 */
	List<Stock> getStockByBoxCode(String boxCode, List<Long> locIdList);

	/**
	 * 获取所有库存数据，含冻结的库存。如果出库暂存区为空则表示查询的库存数据包含出库暂存区的
	 *
	 * @param lpnCode
	 * @param excludeLocIdList
	 * @return
	 */
	List<Stock> getStockByLpnCodeExcludeLoc(String lpnCode, List<Long> excludeLocIdList);

	/**
	 * 根据lpn和loc获取指定库存
	 * @param lpnCode 必填，不能为空
	 * @param locIdList 可为空
	 * @return
	 */
	List<Stock> getStockByLpnCode(String lpnCode, List<Long> locIdList);

	/**
	 * 根据箱码查询该LPN上所有的库存，含自身
	 * @param boxCode 必填，不能为空
	 * @return
	 */
	List<Stock> getStockOnLpnByBoxCode(String boxCode);

	List<Stock> getStock(StockStatusEnum status, Long woId, Long locId,
						 Long skuId, String boxCode, String lpnCode);

	List<Stock> getStockByLocId(Long locId);

	Stock saveNewStock(Stock stock);

	Stock updateStock(Stock stock);

	void updateStock(Long stockId, BigDecimal stockQty, BigDecimal stayStockQty,
				 BigDecimal pickQty, LocalDateTime lastInTime, LocalDateTime lastOutTime);
}
