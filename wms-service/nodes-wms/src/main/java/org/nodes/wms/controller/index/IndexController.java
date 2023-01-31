package org.nodes.wms.controller.index;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.instock.receiveLog.ReceiveLogBiz;
import org.nodes.wms.biz.outstock.logSoPick.LogSoPickBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页API
 **/
@RequestMapping(WmsApiPath.WMS_ROOT_URL+ "index")
@RestController
@RequiredArgsConstructor
public class IndexController {

	private final StockQueryBiz stockQueryBiz;
	private final ReceiveLogBiz receiveLogBiz;

	private final LogSoPickBiz logSoPickBiz;

	/**
	 * 首页：获取首页库存信息
	 */
	/*@GetMapping("/stockData")
	public R<StockIndexResponse> stockData(){
		return R.data(stockQueryBiz.staticsStockDataOnIndexPage());
	}*/

	/**
	 * 首页：获取一周内收货量前10的物品
	 */
	/*@GetMapping("/inStockRate")
	public  R<List<ReceiveLogIndexResponse>> inStockRate(){
		return R.data(receiveLogBiz.findReceiveSkuQtyTop10());
	}*/

	/**
	 * 首页：获取一周内发货量前10的物品
	 */
	/*@GetMapping("/outStockRate")
	public  R<List<LogSoPickIndexResponse>> outStockRate(){
		return R.data(logSoPickBiz.findPickSkuQtyTop10());
	}*/
}
