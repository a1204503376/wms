package org.nodes.wms.controller.stock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.stock.dto.input.StockLogPageQuery;
import org.nodes.wms.dao.stock.dto.output.StockLogPageResponse;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 库存日志API
 **/
@RestController("StockMoveLogController")
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "stockLog")
public class StockLogController {

	private final StockBiz stockBiz;

	@PostMapping("/page")
	public R<IPage<StockLogPageResponse>> page(Query query, @RequestBody StockLogPageQuery stockLogPageQuery) {
		return R.data(stockBiz.pageStockLog(query, stockLogPageQuery));
	}

	@PostMapping("/export")
	public void export(@RequestBody StockLogPageQuery stockLogPageQuery, HttpServletResponse response) {
		stockBiz.export(stockLogPageQuery, response);
	}
}
