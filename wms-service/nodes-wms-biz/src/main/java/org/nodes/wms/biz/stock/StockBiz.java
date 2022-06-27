package org.nodes.wms.biz.stock;

import org.nodes.wms.dao.stock.dto.output.StockIndexResponse;

import java.util.List;
import java.util.Map;

/**
 * 库存业务接口
 **/
public interface StockBiz {


	/**
	 * 首页的库存数据统计
	 * @return
	 */
	StockIndexResponse staticsStockDataOnIndexPage();

}
