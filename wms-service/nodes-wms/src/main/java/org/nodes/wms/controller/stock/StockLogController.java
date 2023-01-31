package org.nodes.wms.controller.stock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
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
@Api(value = "库存日志接口", tags = "库存日志接口")
public class StockLogController {

	private final StockBiz stockBiz;
	private final StockQueryBiz stockQueryBiz;

	/**
	 * 库存日志：分页
	 */
	@ApiOperation(value = "分页")
	@PostMapping("/page")
	public R<IPage<StockLogPageResponse>> page(Query query, @RequestBody StockLogPageQuery stockLogPageQuery) {
		return R.data(stockQueryBiz.pageStockLog(query, stockLogPageQuery));
	}

	/**
	 * 库存日志：服务端导出
	 */
	@ApiOperation(value = "导出")
	@PostMapping("/export")
	public void export(@RequestBody StockLogPageQuery stockLogPageQuery, HttpServletResponse response) {
		stockBiz.exportStockLogToExcel(stockLogPageQuery, response);
	}
}
