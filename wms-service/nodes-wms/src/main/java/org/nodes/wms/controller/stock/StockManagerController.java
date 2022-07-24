package org.nodes.wms.controller.stock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.stock.dto.input.StockImportRequest;
import org.nodes.wms.dao.stock.dto.input.StockPageQuery;
import org.nodes.wms.dao.stock.dto.output.StockPageResponse;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 库存管理API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "stock")
public class StockManagerController {
	private final StockBiz stockBiz;
	private final StockQueryBiz stockQueryBiz;

	@PostMapping("/page")
	public R<IPage<StockPageResponse>> page(Query query, @RequestBody StockPageQuery stockPageQuery) {
		return R.data(stockQueryBiz.getStockPage(query, stockPageQuery));
	}

	/**
	 * 导出
	 */
	@PostMapping("export")
	public void export(@RequestBody StockPageQuery stockPageQuery, HttpServletResponse response) {
		stockBiz.exportStockToExcel(stockPageQuery, response);
	}

	/**
	 * 导入模板
	 */
	@GetMapping("/export-template")
	public void exportTemplate(HttpServletResponse response) {
		List<StockImportRequest> importExcelList = new ArrayList<>();
		ExcelUtil.export(response, "库存余额", "库存余额数据表", importExcelList, StockImportRequest.class);
	}

	/**
	 * 库存余额：库存导入
	 * @param file
	 * @return
	 */
	@ApiLog("库存管理-导入")
	@PostMapping("/import-data")
	@ApiOperation("库存管理-导入")
	public R<String> importData(MultipartFile file) {
		List<StockImportRequest> importDataList = ExcelUtil.read(file, StockImportRequest.class);
		boolean importFlag = stockBiz.importStockByExcel(importDataList);
		return importFlag ? R.success("导入成功") : R.fail("导入失败");
	}


}
