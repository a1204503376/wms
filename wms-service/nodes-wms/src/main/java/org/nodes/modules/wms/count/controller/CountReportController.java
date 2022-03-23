
package org.nodes.modules.wms.count.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.count.entity.CountReport;
import org.nodes.wms.core.count.service.ICountReportService;
import org.nodes.wms.core.count.vo.CountReportVO;
import org.nodes.wms.core.count.wrapper.CountReportWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 盘点差异报告表 控制器
 *
 * @author chz
 * @since 2020-02-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/count/countreport")
@Api(value = "盘点差异报告表", tags = "盘点差异报告表接口")
public class CountReportController extends BladeController {

	private ICountReportService countReportService;

	/**
	 * 详情
	 */
	@ApiLog("盘点差异报告表接口-盘点差异报告表详情")
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "盘点差异报告表详情", notes = "传入countReport")
	public R<CountReport> detail(CountReport countReport) {
		CountReport detail = countReportService.getOne(Condition.getQueryWrapper(countReport));
		return R.data(detail);
	}

	/**
	 * 分页 盘点差异报告表
	 */
	@ApiLog("盘点差异报告表接口-分页")
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "盘点差异报告表分页", notes = "传入countReport")
	public R<IPage<CountReport>> list(CountReport countReport, Query query) {
		IPage<CountReport> pages = countReportService.page(Condition.getPage(query), Condition.getQueryWrapper(countReport));
		return R.data(pages);
	}

	/**
	 * 自定义分页 盘点差异报告表
	 */
	@ApiLog("盘点差异报告表接口-自定义分页")
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "自定义分页盘点差异报告表", notes = "传入countReport")
	public R<IPage<CountReportVO>> page(CountReportVO countReport, Query query) {
		IPage<CountReport> pages = countReportService.page(
			Condition.getPage(query), Condition.getQueryWrapper(countReport));
		return R.data(CountReportWrapper.build().pageVO(pages));
	}

	/**
	 * 新增 盘点差异报告表
	 */
	@ApiLog("盘点差异报告表接口-新增")
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增盘点差异报告表", notes = "传入countReport")
	public R save(@Valid @RequestBody CountReport countReport) {
		return R.status(countReportService.save(countReport));
	}

	/**
	 * 修改 盘点差异报告表
	 */
	@ApiLog("盘点差异报告表接口-修改")
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改盘点差异报告表", notes = "传入countReport")
	public R update(@Valid @RequestBody CountReport countReport) {
		return R.status(countReportService.updateById(countReport));
	}

	/**
	 * 新增或修改 盘点差异报告表
	 */
	@ApiLog("盘点差异报告表接口-新增或修改")
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改盘点差异报告表", notes = "传入countReport")
	public R submit(@Valid @RequestBody CountReport countReport) {
		return R.status(countReportService.saveOrUpdate(countReport));
	}


	/**
	 * 删除 盘点差异报告表
	 */
	@ApiLog("盘点差异报告表接口-删除")
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除盘点差异报告表", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(countReportService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 差异报告导出
	 *
	 * @param id
	 * @param response
	 */
	@ApiLog("盘点差异报告表接口-差异报告导出")
	@GetMapping("/export")
	@ApiOperation(value = "差异报告导出", notes = "传入盘点单ID")
	public void exportAccounts(@RequestParam String id, HttpServletResponse response) {
		countReportService.exportCountReportsExcel(id, response);
	}
}
