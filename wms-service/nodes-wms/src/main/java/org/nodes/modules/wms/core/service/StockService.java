package org.nodes.modules.wms.core.service;

import org.nodes.wms.dao.stock.entities.Stock;

import java.util.List;


public interface StockService {

	/**
	 * 内部转移
	 * @param stockList 库存集合
	 * @return
	 */
	boolean updateBatch(List<Stock> stockList);

}
