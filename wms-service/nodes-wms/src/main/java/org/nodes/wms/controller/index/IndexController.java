package org.nodes.wms.controller.index;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.instock.receiveLog.ReceiveLogBiz;
import org.nodes.wms.biz.outstock.logSoPick.LogSoPickBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogIndexResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickIndexResponse;
import org.nodes.wms.dao.stock.dto.output.StockIndexResponse;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

	@GetMapping("/stockData")
	public R<StockIndexResponse> stockData(){
		return R.data(stockQueryBiz.staticsStockDataOnIndexPage());
	}

	@GetMapping("/inStockRate")
	public  R<List<ReceiveLogIndexResponse>> inStockRate(){
		return R.data(receiveLogBiz.findReceiveSkuQtyTop10());
	}

	@GetMapping("/outStockRate")
	public  R<List<LogSoPickIndexResponse>> outStockRate(){
		return R.data(logSoPickBiz.findPickSkuQtyTop10());
	}
}
