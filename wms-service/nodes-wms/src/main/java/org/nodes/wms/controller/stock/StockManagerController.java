package org.nodes.wms.controller.stock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.lendreturn.LendReturnBiz;
import org.nodes.wms.biz.stock.SerialBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.stockManage.StockManageBiz;
import org.nodes.wms.dao.lendreturn.dto.input.LendReturnQuery;
import org.nodes.wms.dao.lendreturn.dto.output.NoReturnResponse;
import org.nodes.wms.dao.stock.dto.input.*;
import org.nodes.wms.dao.stock.dto.output.SerialPageResponse;
import org.nodes.wms.dao.stock.dto.output.StockBySerialPageResponse;
import org.nodes.wms.dao.stock.dto.output.StockMoveResponse;
import org.nodes.wms.dao.stock.dto.output.StockPageResponse;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 库存管理API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "stock")
@Api(value = "库存管理", tags = "库存管理接口")
public class StockManagerController {
	private final StockBiz stockBiz;
	private final StockQueryBiz stockQueryBiz;
	private final StockManageBiz stockManageBiz;
	private final LendReturnBiz lendReturnBiz;
	private final SerialBiz serialBiz;

	/**
	 * 库存余额：分页
	 */
	@ApiOperation(value = "库存余额分页查询")
	@PostMapping("/page")
	public R<IPage<StockPageResponse>> page(Query query, @RequestBody StockPageQuery stockPageQuery) {
		return R.data(stockQueryBiz.getStockPage(query, stockPageQuery));
	}

	/**
	 * 库存统计：导出
	 */
	@ApiOperation(value = "库存统计导出")
	@PostMapping("exportStockListCount")
	public void exportStockListCount(@RequestBody StockPageQuery stockPageQuery, HttpServletResponse response) {
		stockQueryBiz.getStockListCount(stockPageQuery, response);
	}

	/**
	 * 库存余额：导出
	 */
	@ApiOperation(value = "库存余额导出")
	@PostMapping("export")
	public void export(@RequestBody StockPageQuery stockPageQuery, HttpServletResponse response) {
		stockBiz.exportStockToExcel(stockPageQuery, response);
	}

	/**
	 * 库存余额：导入模板
	 */
	@ApiOperation(value = "导出Excel模板")
	@GetMapping("/export-template")
	public void exportTemplate(HttpServletResponse response) {
		List<StockImportRequest> importExcelList = new ArrayList<>();
		ExcelUtil.export(response, "库存余额", "库存余额数据表", importExcelList, StockImportRequest.class);
	}

	/**
	 * 库存余额：库存导入
	 */
	@ApiLog("库存管理-导入")
	@ApiOperation("库存管理-导入")
	@PostMapping("/import-data")
	public R<String> importData(MultipartFile file) {
		List<StockImportRequest> importDataList = ExcelUtil.read(file, StockImportRequest.class);
		boolean importFlag = stockBiz.importStockByExcel(importDataList);
		return importFlag ? R.success("导入成功") : R.fail("导入失败");
	}

	/**
	 * 库存余额：按件移动-根据库存id获取库存信息
	 */
	@ApiOperation(value = "按件移动-根据库存id获取库存信息")
	@PostMapping("/getStockDataByStockId")
	public R<StockMoveResponse> getStockDataByStockId(
		@Valid @RequestBody StockIdRequest stockIdRequest) {
		Stock stock = stockQueryBiz.findStockById(stockIdRequest.getStockId());
		StockMoveResponse response = Func.copy(stock, StockMoveResponse.class);
		return R.data(response);
	}

	/**
	 * 库存余额：按箱移动-根据箱码获取库存信息
	 */
	@ApiOperation(value = "按箱移动-根据箱码获取库存信息")
	@PostMapping("/getStockDataByBoxCode")
	public R<List<StockMoveResponse>> getStockDataToMove(
		@Valid @RequestBody StockMoveByBoxCodeRequest stockMoveByBoxCodeRequest) {
		List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(stockMoveByBoxCodeRequest.getBoxCodeList());
		List<StockMoveResponse> responseList = BeanUtil.copy(stockList, StockMoveResponse.class);
		return R.data(responseList);
	}

	/**
	 * 库存余额：按件移动
	 */
	@ApiOperation(value = "按件移动")
	@PostMapping("move")
	public R<String> move(@Valid @RequestBody StockPcMoveRequest stockPcMoveRequest) {
		stockManageBiz.stockMoveByPc(stockPcMoveRequest);
		return R.success("操作成功");
	}

	/**
	 * 库存余额：按箱移动
	 */
	@ApiOperation(value = "按箱移动")
	@PostMapping("/moveByBox")
	public R<String> moveByBoxCode(@Valid @RequestBody StockMoveByBoxCodeRequest stockMoveByBoxCodeRequest) {
		stockManageBiz.stockMoveByBox(stockMoveByBoxCodeRequest);
		return R.success("操作成功");
	}

	/**
	 * 库存余额:库存冻结
	 *
	 * @param stockThawAndFrozenDto 前端传入参数
	 * @return 操作成功提示
	 */
	@ApiOperation(value = "库存冻结")
	@PostMapping("/stockFrozen")
	public R<String> stockFrozen(@RequestBody StockThawAndFrozenDto stockThawAndFrozenDto) {
		stockManageBiz.stockFrozen(stockThawAndFrozenDto);
		return R.success("操作成功");
	}

	/**
	 * 库存余额:库存解冻
	 *
	 * @param stockThawAndFrozenDto 前端传入参数
	 * @return 操作成功提示
	 */
	@ApiOperation(value = "库存解冻")
	@PostMapping("/stockUnFrozen")
	public R<String> stockUnFrozen(@RequestBody StockThawAndFrozenDto stockThawAndFrozenDto) {
		stockManageBiz.stockUnFrozen(stockThawAndFrozenDto);
		return R.success("操作成功");
	}

	/**
	 * 库存余额: 按序列号显示
	 *
	 * @param query                  分页参数
	 * @param stockBySerialPageQuery 查询条件
	 * @return 分页返回对象
	 */
	@ApiOperation(value = "按序列号显示")
	@PostMapping("/pageBySerial")
	public R<IPage<StockBySerialPageResponse>> pageBySerial(Query query, @RequestBody StockBySerialPageQuery stockBySerialPageQuery) {
		return R.data(stockQueryBiz.getStockBySerialPage(query, stockBySerialPageQuery));
	}

	/**
	 * 库存余额: 按序列号显示导出
	 *
	 * @param stockBySerialPageQuery 查询参数
	 * @param response               响应对象
	 */
	@ApiOperation(value = "按序列号导出")
	@PostMapping("exportBySerial")
	public void exportBySerial(@RequestBody StockBySerialPageQuery stockBySerialPageQuery, HttpServletResponse response) {
		stockBiz.exportBySerial(stockBySerialPageQuery, response);
	}

	/**
	 * 未归还列表：分页查询
	 */
	@ApiOperation(value = "未归还列表分页查询")
	@PostMapping("/pageUnReturned")
	public R<Page<NoReturnResponse>> pageUnReturned(Query query, @RequestBody LendReturnQuery lendReturnQuery) {
		Page<NoReturnResponse> noReturnResponsePage = lendReturnBiz.pageNoReturn((Page) Condition.getPage(query), lendReturnQuery);
		return R.data(noReturnResponsePage);
	}

	/**
	 * 未归还列表：服务端导出
	 */
	@ApiOperation(value = "未归还列表导出")
	@PostMapping("/exportUnReturned")
	public void pageUnReturned(@RequestBody LendReturnQuery lendReturnQuery, HttpServletResponse response) {
		lendReturnBiz.exportNoReturn(lendReturnQuery, response);
	}

	/**
	 * 序列号：分页查询
	 */
	@ApiOperation(value = "序列号分页查询")
	@PostMapping("/pageSerial")
	public R<Page<SerialPageResponse>> pageSerial(@RequestBody @Valid SerialPageQuery serialPageQuery, Query query) {
		return R.data(serialBiz.page(serialPageQuery, query));
	}

	/**
	 * 序列号：服务端导出
	 */
	@ApiOperation(value = "序列号导出")
	@PostMapping("/exportSerial")
	public void exportSerial(@RequestBody @Valid SerialPageQuery serialPageQuery, HttpServletResponse response) {
		serialBiz.export(serialPageQuery, response);
	}
}
