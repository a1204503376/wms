package org.nodes.wms.controller.outstock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.core.tool.validation.Update;
import org.nodes.wms.biz.outstock.OutStockBiz;
import org.nodes.wms.biz.outstock.logSoPick.LogSoPickBiz;
import org.nodes.wms.biz.outstock.plan.SoPickPlanBiz;
import org.nodes.wms.biz.outstock.so.SoBillBiz;
import org.nodes.wms.dao.common.log.dto.output.LogDetailPageResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.NotSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickForSoDetailResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickPageResponse;
import org.nodes.wms.dao.outstock.so.dto.input.*;
import org.nodes.wms.dao.outstock.so.dto.output.*;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.soPickPlan.dto.intput.SoPickPlanPageQuery;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanPageResponse;
import org.nodes.wms.dao.stock.dto.output.SerialSelectResponse;
import org.nodes.wms.dao.stock.dto.output.StockSoPickPlanResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 发货单API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "outstock/soBill")
public class SoBillController {

	private final SoBillBiz soBillBiz;

	private final LogSoPickBiz logSoPickBiz;

	private final OutStockBiz outStockBiz;
	private final SoPickPlanBiz soPickPlanBiz;

	/**
	 * 发货单: 分页查找
	 */
	@PostMapping("/page")
	public R<Page<SoHeaderPageResponse>> page(Query query, @RequestBody SoHeaderPageQuery soHeaderPageQuery) {
		Page<SoHeaderPageResponse> soHeaderPageList = soBillBiz.getPage(query, soHeaderPageQuery);
		return R.data(soHeaderPageList);
	}

	/**
	 * 发货单：新增
	 */
	@ApiLog("发货单管理-新增")
	@PostMapping("/add")
	public R<String> add(@Valid @RequestBody SoBillAddOrEditRequest soBillAddOrEditRequest) {
		SoHeader soHeader = soBillBiz.add(soBillAddOrEditRequest);
		return R.success(String.format("新增发货单成功，发货单编码：%s", soHeader.getSoBillNo()));
	}

	/**
	 * 发货单：删除
	 */
	@ApiLog("发货单管理-删除")
	@PostMapping("/remove")
	public R<String> remove(@Valid @RequestBody SoBillRemoveRequest soBillRemoveRequest) {
		boolean isRemoveSuccess = soBillBiz.remove(soBillRemoveRequest.getIdList());
		return R.success(isRemoveSuccess ? "删除成功" : "删除失败，请稍后再试");
	}

	/**
	 * 发货单编辑：根据发货单查找发货单信息
	 */
	@PostMapping("/detailByEdit")
	public R<SoBillEditResponse> detailByEdit(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		return R.data(soBillBiz.findSoBillByEdit(soBillIdRequest.getSoBillId()));
	}

	/**
	 * 发货单：编辑
	 */
	@ApiLog("发货单管理-编辑")
	@PostMapping("/edit")
	public R<String> edit(@Validated(Update.class) @RequestBody SoBillAddOrEditRequest soBillAddOrEditRequest) {
		SoHeader soHeader = soBillBiz.edit(soBillAddOrEditRequest);
		return R.success(String.format("编辑发货单成功，发货单编码：%s", soHeader.getSoBillNo()));
	}

	/**
	 * 发货单明细：根据发货单id查找发货单头表信息
	 */
	@PostMapping("/detail_header")
	public R<SoHeaderForDetailResponse> headerForDetail(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		return R.data(soBillBiz.findSoHeaderForDetailBySoBillId(soBillIdRequest.getSoBillId()));
	}

	/**
	 * 发货单明细：根据发货单id查找发货明细信息
	 */
	@PostMapping("/detail_detail")
	public R<Page<SoDetailForDetailResponse>> detailForDetail(Query query,
															  @Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		Page<SoDetailForDetailResponse> pageSoDetail = soBillBiz.pageSoDetailForDetailBySoBillId(query, soBillIdRequest);
		return R.data(pageSoDetail);
	}

	/**
	 * 发货单明细：根据发货单id查找发货单拣货记录信息
	 */
	@PostMapping("/detail_logSoPick")
	public R<Page<LogSoPickForSoDetailResponse>> logSoPickForSoDetail(Query query,
																	  @Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		Page<LogSoPickForSoDetailResponse> pageLogSoPick = logSoPickBiz.pageLogSoPickForSoDetailBySoBillId(query, soBillIdRequest);
		return R.data(pageLogSoPick);
	}

	/**
	 * 发货单明细：根据发货单id查找发货单审计日志信息
	 */
	@PostMapping("/detail_log")
	public R<Page<LogDetailPageResponse>> logForSoDetail(Query query,
														 @Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		Page<LogDetailPageResponse> pageLog = soBillBiz.pageLogById(Condition.getPage(query), soBillIdRequest.getSoBillId());
		return R.data(pageLog);
	}

	/**
	 * 发货单导出：服务端导出
	 */
	@PostMapping("/export")
	public void export(@RequestBody SoHeaderPageQuery soHeaderPageQuery, HttpServletResponse response) {
		soBillBiz.export(soHeaderPageQuery, response);
	}

	/**
	 * 发货单关闭：根据发货单id关闭发货单
	 */
	@PostMapping("/close")
	@ApiLog("发货单-关闭")
	public R<String> close(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		soBillBiz.closeById(soBillIdRequest.getSoBillId());
		return R.success("关闭成功");
	}

	/**
	 * 未发货记录：分页查找
	 */
	@PostMapping("/pageNotSoPick")
	public R<IPage<NotSoPickPageResponse>> pageNotLogSoPick(
		Query query, @RequestBody NotSoPickPageQuery notSoPickPageQuery) {
		return R.data(soBillBiz.pageNotSoPick(query, notSoPickPageQuery));
	}

	/**
	 * 未发货记录：服务端导出
	 */
	@PostMapping("/exportNotSoPick")
	public void exportNotSoPick(
		@RequestBody NotSoPickPageQuery notSoPickPageQuery, HttpServletResponse response) {
		soBillBiz.exportNotSoPick(notSoPickPageQuery, response);
	}

	/**
	 * PC拣货：获取发货单头表信息
	 */
	@PostMapping("/getSoHeaderByPickPc")
	public R<PickByPcSoHeaderResponse> getSoHeaderByPickPc(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		return R.data(soBillBiz.getSoHeaderByPickPc(soBillIdRequest));
	}

	/**
	 * PC拣货：获取发货但可用拣货的明细简要信息
	 */
	@GetMapping("/getLineNoAndSkuSelectList")
	public R<List<LineNoAndSkuSelectResponse>> getLineNoAndSkuSelectList(Long soBillId) {
		return R.data(soBillBiz.getLineNoAndSkuSelectList(soBillId));
	}

	/**
	 * 分配：获取分配页面发货单头表和明细信息
	 */
	@PostMapping("/getSoBillDataByDistribution")
	public R<SoBillDistributedResponse> getSoBillDataByDistribution(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		return R.data(soBillBiz.findSoBillForDistributeBySoBillId(soBillIdRequest.getSoBillId()));
	}

	/**
	 * PC拣货：根据明细行号获取明细和对应的可用库存
	 */
	@PostMapping("/getSoDetailAndStock")
	public R<SoDetailAndStockResponse> getSoDetailAndStock(@Valid @RequestBody
														   SoDetailAndStockRequest soDetailAndStockRequest) {
		return R.data(soBillBiz.getSoDetailAndStock(soDetailAndStockRequest));
	}

	/**
	 * 分配：根据发货单id和发货明细获取分配信息
	 */
	@PostMapping("/getSoPickPlan")
	public R<List<SoPickPlanForDistributionResponse>> getSoPickPlan(
		@Valid @RequestBody SoBillIdAndSoDetailIdRequest request) {
		return R.data(outStockBiz.getSoPickPlanBySoBillIdAndSoDetailId(request.getSoBillId(), request.getSoDetailId()));
	}

	/**
	 * 分配：自动分配
	 */
	@ApiLog("发货单管理-自动分配")
	@PostMapping("/automaticAssign")
	public R<String> automaticAssign(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		return R.success(outStockBiz.autoDistribute(soBillIdRequest.getSoBillId()));
	}

	/**
	 * 分配：全部取消
	 */
	@ApiLog("发货单管理-取消分配")
	@PostMapping("/cancelAll")
	public R<String> cancelAll(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		outStockBiz.cancelDistribute(soBillIdRequest.getSoBillId());
		return R.success("取消分配成功");
	}

	/**
	 * 分配：确认下发
	 */
	@ApiLog("发货单管理-确认下发")
	@PostMapping("/issued")
	public R<String> issued(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		outStockBiz.issued(soBillIdRequest.getSoBillId());
		return R.success("确认下发成功");
	}

	/**
	 * 分配调整：根据物品id查找物品可分配库存信息
	 */
	@PostMapping("/getStockByDistributeAdjust")
	public R<List<StockSoPickPlanResponse>> getStockByDistributeAdjust(
							@Valid @RequestBody DistributeAdjustRequest distributeAdjustRequest) {
		return R.data(outStockBiz.getStockByDistributeAdjust(
			distributeAdjustRequest.getSkuId(),
			distributeAdjustRequest.getSkuLot1(),
			distributeAdjustRequest.getSkuLot4(),
			distributeAdjustRequest.getSoBillId()));
	}

	/**
	 * 分配调整：保存分配信息、更新库存占用数量
	 */
	@ApiLog("发货单管理-分配调整")
	@PostMapping("/saveAssign")
	public R<String> saveAssign(@Valid @RequestBody SoBillDistributedRequest soBillDistributedRequest) {
		outStockBiz.manualDistribute(soBillDistributedRequest);
		return R.data("调整成功");
	}

	/**
	 * pc拣货: 获取序列号下拉列表
	 */
	@GetMapping("getSerialSelectResponseList")
	public R<List<SerialSelectResponse>> getSerialSelectResponseList(Long stockId) {
		List<SerialSelectResponse> serialSelectResponseList = soBillBiz.getSerialSelectResponseList(stockId);
		return R.data(serialSelectResponseList);
	}

	/**
	 * pc拣货
	 */
	@ApiLog("pc拣货")
	@PostMapping("pickByPc")
	public R<String> pickByPc(@RequestBody PickByPcRequest pickByPcRequest) {
		outStockBiz.pickByPcsOnPc(pickByPcRequest);
		return R.data("操作成功");
	}

	/**
	 * 分配记录：分页
	 */
	@PostMapping("/getSoPickPlanpage")
	public R<Page<SoPickPlanPageResponse>> getSoPickPlanpage(Query query, @RequestBody SoPickPlanPageQuery soPickPlanPageQuery) {
		Page<SoPickPlanPageResponse> soPickPlanPage = soPickPlanBiz.page(query, soPickPlanPageQuery);
		return R.data(soPickPlanPage);
	}

	/**
	 * 分配记录：服务端导出
	 */
	@PostMapping("/exportSoPickPlan")
	public void exportSoPickPlan(@RequestBody SoPickPlanPageQuery soPickPlanPageQuery, HttpServletResponse response) {
		soPickPlanBiz.export(soPickPlanPageQuery, response);
	}
}
