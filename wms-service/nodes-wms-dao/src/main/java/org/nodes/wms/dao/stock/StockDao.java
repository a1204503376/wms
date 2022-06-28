package org.nodes.wms.dao.stock;

import org.nodes.wms.dao.stock.entities.Stock;

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
	 * @param skuCode 物品编码
	 * @param skuName 物品名称
	 * @param locCode 库位编码
	 * @return 库存信息
	 */
	Stock getStockBySku(String skuCode, String skuName, String locCode);
}
