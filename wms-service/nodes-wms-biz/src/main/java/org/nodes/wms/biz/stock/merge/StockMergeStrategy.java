package org.nodes.wms.biz.stock.merge;

import org.nodes.wms.dao.stock.entities.Stock;

/**
 * 库存合并策略
 */
public interface StockMergeStrategy {

	/**
	 * 库存是否需要合并，如果不需要合并返回空的库存，如果需要合并返回旧的库存
	 *
	 * @param newStock 新的库存数据
	 * @return 可以合并的数据库中已有的库存
	 */
	Stock apply(Stock newStock);
}
