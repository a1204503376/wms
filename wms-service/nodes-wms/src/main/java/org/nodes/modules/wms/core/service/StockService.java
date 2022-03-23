package org.nodes.modules.wms.core.service;

import org.nodes.wms.core.stock.core.entity.Stock;

import java.util.List;


public interface StockService {

	/**
	 * 内部转移
	 * @param stockList 库存集合
	 * @return
	 */
	boolean updateBatch(List<Stock> stockList);

}
