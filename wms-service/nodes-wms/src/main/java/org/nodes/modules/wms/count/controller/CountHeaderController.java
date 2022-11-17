package org.nodes.modules.wms.count.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.wms.biz.count.StockCountBiz;
import org.nodes.wms.core.count.entity.CountHeader;
import org.nodes.wms.core.count.service.ICountHeaderService;
import org.nodes.wms.core.count.vo.CountHeaderVO;
import org.nodes.wms.core.count.wrapper.CountHeaderWrapper;
import org.nodes.wms.dao.count.dto.input.PdaStockCountDetailBySkuSpecRequest;
import org.nodes.wms.dao.count.dto.output.PdaStockCountDetailBySkuSpecResponse;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * 盘点单头表 控制器
 *
 * @author chz
 * @since 2020-01-02
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/count/countheader")
@Api(value = "盘点单头表", tags = "盘点单头表接口")
public class CountHeaderController extends BladeController {

	private ICountHeaderService countHeaderService;
	private StockCountBiz stockCountBiz;

	/**
	 * 获取盘点单详情
	 */
	@ApiLog("盘点单头表接口-获取盘点单详情")
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "获取盘点单详情", notes = "传入countHeader")
	public R<CountHeader> detail(CountHeader countHeader) {
		CountHeaderVO detail = countHeaderService.getDetail(countHeader);
		return R.data(detail);
	}

	/**
	 * 分页 盘点单头表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页 盘点单头表", notes = "传入countHeader")
	public R<IPage<CountHeader>> list(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<CountHeader> pages = countHeaderService.page(Condition.getPage(query),
			Condition.getQueryWrapper(params, CountHeader.class));
		return R.data(pages);
	}

	/**
	 * 自定义分页 盘点单头表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "自定义分页 盘点单头表", notes = "传入countHeader")
	public R<IPage<CountHeaderVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<CountHeader> pages = countHeaderService.page(Condition.getPage(query), Condition.getQueryWrapper(params, CountHeader.class));
		return R.data(CountHeaderWrapper.build().pageVO(pages));
	}

	/**
	 * 新增 盘点单头表
	 */
	@ApiLog("盘点单头表接口-新增")
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增 盘点单头表", notes = "传入countHeader")
	public R save(@RequestBody CountHeaderVO countHeader) {
		return (R.status(countHeaderService.add(countHeader)));
	}

	/**
	 * 修改 盘点单头表
	 */
	@ApiLog("盘点单头表接口-修改")
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入countHeader")
	public R update(@Valid @RequestBody CountHeader countHeader) {
		return R.status(countHeaderService.updateById(countHeader));
	}

	/**
	 * 新增或修改 盘点单头表
	 */
	@ApiLog("盘点单头表接口-新增或修改")
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改 盘点单头表", notes = "传入countHeader")
	public R submit(@RequestBody CountHeaderVO countHeaderVO) {
		if (Func.isEmpty(countHeaderVO.getCountBillId())) {
			return R.status(countHeaderService.add(countHeaderVO));
		} else {
			return R.status(countHeaderService.updateById(countHeaderVO));
		}
	}


	/**
	 * 删除 盘点单头表
	 */
	@ApiLog("盘点单头表接口-删除盘点单头表")
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除盘点单头表", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(countHeaderService.removeByIdsNew(ids));
	}


	/**
	 * 筛选盘点货位信息
	 */
	@ApiLog("盘点单头表接口-筛选货位信息")
	@PostMapping("/querylocation")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "筛选货位信息", notes = "countHeaderVO")
	public R<List<CountHeaderVO>> querylocation(@RequestBody CountHeaderVO countHeaderVO) {

		return R.data(countHeaderService.selectLocation(countHeaderVO));
	}

	/**
	 * 筛选盘点货位信息
	 */
	@PostMapping("/findStockCountDetailBySkuSpec")
	public R<List<PdaStockCountDetailBySkuSpecResponse>> findStockCountDetailBySkuSpec(@RequestBody PdaStockCountDetailBySkuSpecRequest request) {
		return R.data(stockCountBiz.findStockCountDetailBySkuSpec(request));
	}

	/**
	 * 获取盘点单编号
	 */
	@ApiLog("盘点单头表接口-获取盘点单编号")
	@PostMapping("/getCNo")
	@ApiOperationSupport(order = 11)
	public R getCNo() {

		return R.data(countHeaderService.getCNo());
	}

	/**
	 * 获取盘点默认模式
	 */
	@ApiLog("盘点单头表接口-获取盘点默认模式")
	@PostMapping("/getDefaltMode")
	public R getDefaltMode() {
		return R.data(ParamCache.getValue(ParamEnum.COUNT_MODE.getKey()));
	}

	/**
	 * 分配任务
	 */
	@ApiLog("盘点单头表接口-分配任务")
	@PostMapping("/countTask")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "分配任务", notes = "countHeaderVO")
	public R countTask(@RequestBody CountHeaderVO countHeaderVO) {
		return R.data(countHeaderService.countTask(countHeaderVO));
	}


	/**
	 * 完成盘点单
	 */
	@ApiLog("盘点单头表接口-完成盘点单")
	@PostMapping("/completeTask")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "完成盘点单", notes = "传入countBillId")
	public R completeTask(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(countHeaderService.completeTask(ids));
	}

	/**
	 * 生成差异报告
	 */
	@ApiLog("盘点单头表接口-生成差异报告")
	@PostMapping("/differenceReport")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "生成差异报告", notes = "传入countBillId")
	public R differenceReport(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
//		return R.status(countHeaderService.differenceReport(ids));
		return R.status(countHeaderService.generateDifference(ids));

	}

	/**
	 * 创建差异单据
	 */
	@ApiLog("盘点单头表接口-创建差异单据")
	@PostMapping("/differenceOrder")
	@ApiOperationSupport(order = 17)
	@ApiOperation(value = "创建差异单据", notes = "传入countBillId")
	public R differenceOrder(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(countHeaderService.differenceOrder(ids));
	}


	/**
	 * 生成随机盘点差异报告
	 */
	@ApiLog("盘点单头表接口-生成随机盘点差异报告")
	@PostMapping("/randomCountDifferenceReport")
	@ApiOperationSupport(order = 11)
	@ApiOperation(value = "生成差异报告", notes = "传入countHeaderVO")
	public R randomCountDifferenceReport(@RequestBody CountHeaderVO countHeaderVO) {
		return R.status(countHeaderService.randomCountDifferenceReport(countHeaderVO.getCountRecordVOList()));
	}

	@ApiLog("盘点单头表接口-差异报告作废")
	@PostMapping("/report/cancel")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "差异报告作废", notes = "传入盘点单主键")
	public R reportCancel(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(countHeaderService.reportCancel(Func.toLongList(ids)));
	}

	@ApiLog("盘点单头表接口-复盘")
	@PostMapping("/repeat")
	@ApiOperationSupport(order = 14)
	@ApiOperation(value = "复盘", notes = "传入需要复盘的盘点单主键")
	public R<String> repeat(@ApiParam(value = "盘点单主键ID", required = true) @RequestParam String ids) {
		return R.data(countHeaderService.repeat(ids));
	}

	/**
	 * 盘点单批量作废
	 *
	 * @param ids
	 * @return
	 */
	@ApiLog("盘点单头表接口-盘点单批量作废")
	@PostMapping("/abolish")
	@ApiOperationSupport(order = 15)
	@ApiOperation(value = "盘点单批量作废", notes = "传入盘点头表ID列表")
	public R abolish(@ApiParam(value = "盘点单主键ID", required = true) @RequestParam String ids) {
		return R.status(countHeaderService.abolishCountHeader(ids));
	}

	/**
	 * 随机盘点单批量作废
	 *
	 * @param ids
	 * @return
	 */
	@ApiLog("盘点单头表接口-随机盘点单批量作废")
	@PostMapping("/abolishRandomCount")
	@ApiOperationSupport(order = 16)
	@ApiOperation(value = "随机盘点单批量作废", notes = "传入盘点头表ID列表")
	public R abolishRandomCount(@ApiParam(value = "盘点记录ID", required = true) @RequestParam String ids) {
		return R.status(countHeaderService.abolishRandomCount(ids));
	}
}
