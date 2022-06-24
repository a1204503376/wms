package org.nodes.wms.biz.stock;

import org.nodes.wms.dao.stock.dto.output.StockIndexResponse;

import java.util.List;
import java.util.Map;

/**
 * 库存业务接口
 **/
public interface StockBiz {

	/**
	 *
	 *
	 * @return
	 */
	StockIndexResponse findStockIndexData();

	/**
	 * 根据库位id查找库存物品总数
	 *
	 * @param locIdList: 库位id集合
	 * @return Integer
	 */
	Integer findStockSkuCountByLocIdList(List<Long> locIdList);
}
