package org.nodes.wms.dao.stock;

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
	 * @return Map<String,Object>
	 */
	Map<String,Object> getStockQtyByLocIdList(List<Long> locIdList);

	/**
	 * 根据库位id获取库存物品总数
	 *
	 * @param locIdList:
	 * @return Integer
	 */
	Integer getStockSkuCountByLocIdList(List<Long> locIdList);
}
