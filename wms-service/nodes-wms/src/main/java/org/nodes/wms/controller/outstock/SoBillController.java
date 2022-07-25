package org.nodes.wms.controller.outstock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.core.tool.validation.Update;
import org.nodes.wms.biz.outstock.logSoPick.LogSoPickBiz;
import org.nodes.wms.biz.outstock.so.SoDetailBiz;
import org.nodes.wms.biz.outstock.so.SoHeaderBiz;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.dao.basics.sku.dto.input.SkuIdRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.NotSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickForSoDetailResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickPageResponse;
import org.nodes.wms.dao.outstock.so.dto.input.*;
import org.nodes.wms.dao.outstock.so.dto.output.*;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.dao.stock.dto.output.StockSoPickPlanResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.ConvertUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 出库单API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "outstock/soBill")
@Api(value = "出库管理主接口", tags = "出库管理主接口")
public class SoBillController {

	private final ISoHeaderService soHeaderService;

	private final SoHeaderBiz soHeaderBiz;

	private final SoDetailBiz soDetailBiz;

	private final LogSoPickBiz logSoPickBiz;

	/**
	 * 详情
	 */
//	@ApiLog("出库管理主接口-详情")
//	@GetMapping("/detail")
//	@ApiOperation(value = "出库单头表详情", notes = "传入出库单id,query")
//	public R<SoHeaderVO> detail(@RequestParam Long soBillId) {
//		return R.data(soHeaderService.getDetail(soBillId));
//	}

	/**
	 * 出库单列表
	 */
//	@ApiLog("出库管理主接口-列表")
//	@GetMapping("/list")
//	@ApiOperation(value = "列表", notes = "传入header")
//	public R<List<SoHeaderVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
//		List<SoHeader> list = soHeaderService.list(Condition.getQueryWrapper(params, SoHeader.class));
//		return R.data(SoHeaderWrapper.build().listVO(list));
//	}

	/**
	 * 出库单分页
	 */
//	@ApiLog("出库管理主接口-分页")
//	@GetMapping("/page")
//	@ApiOperation(value = "分页", notes = "传入soHeader,query")
//	public R<IPage<SoHeaderVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
//		IPage<SoHeader> page = soHeaderService.page(Condition.getPage(query), Condition.getQueryWrapper(params, SoHeader.class));
//		return R.data(SoHeaderWrapper.build().pageVO(page));
//	}

	/**
	 * 新增或修改
	 */
//	@ApiLog("出库管理主接口-新增或修改")
//	@PostMapping("/submit")
//	@ApiOperation(value = "新增或修改", notes = "传入header")
//	public R submit(@Valid @RequestBody SoHeaderDTO header) {
//		return R.status(soHeaderService.saveOrUpdate(header));
//	}

	/**
	 * 删除
	 */
//	@ApiLog("出库管理主接口-删除")
//	@PostMapping("/remove")
//	@ApiOperation(value = "删除", notes = "传入ids")
//	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
//		return R.status(soHeaderService.removeByIds(Func.toLongList(ids)));
//	}

	/**
	 * 获得出库单编号
	 *
	 * @return
	 */
//	@ApiLog("出库管理主接口-获得出库单编号")
//	@GetMapping("/getSoBillNo")
//	@ApiOperation(value = "获得出库单编号", notes = "")
//	public R<String> getSoBillNo() {
//		return R.data(SoCache.getSoBillNo());
//	}

	/**
	 * 获取出库单是否允许编辑
	 *
	 * @param soHeaderId 出库单ID
	 * @return true:允许编辑， false:不允许编辑
	 */
//	@ApiLog("出库管理主接口-获取出库单是否允许编辑")
//	@GetMapping("/canEdit")
//	@ApiOperation(value = "获取出库单是否允许编辑", notes = "传入出库单ID, 获取出库单是否允许编辑")
//	public R canEdit(@Valid @ApiParam(value = "入库单ID", required = true) @RequestParam Long soHeaderId) {
//		return R.data(soHeaderService.canEdit(soHeaderId));
//	}

	/**
	 * 取消订单
	 *
	 * @param ids 出库单ID
	 * @return
	 */
//	@ApiLog("出库管理主接口-取消订单")
//	@PostMapping("/cancel")
//	@ApiOperation(value = "取消订单", notes = "传入订单主键ID")
//	public R cancel(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
//		return R.data(soHeaderService.cancel(Func.toLongList(ids)));
//	}

//	@ApiLog("出库单头表接口-完成订单")
//	@PostMapping("/completed")
//	@ApiOperation(value = "出库单ID", notes = "出库单ID，多个用英文逗号分隔")
//	public R completed(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
//		return R.data(soHeaderService.completed(Func.toLongList(ids)));
//	}

//	@ApiLog("出库单头表接口-完成出库")
//	@PostMapping("/complated/outstock")
//	@ApiOperation(value = "出库单ID", notes = "出库单ID，多个用英文逗号分隔")
//	public R completedOutstock(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
//		return R.data(soHeaderService.completedOutstock(Func.toLongList(ids)));
//	}

	/**
	 * 发货单: 分页查找
	 */
	@PostMapping("/page")
	public R<Page<SoHeaderPageResponse>> page(Query query, @RequestBody SoHeaderPageQuery soHeaderPageQuery) {
		Page<SoHeaderPageResponse> soHeaderPageList = soHeaderBiz.getPage(query, soHeaderPageQuery);
		return R.data(soHeaderPageList);
	}

	/**
	 * 发货单：新增
	 */
	@ApiLog("出库单管理-新增")
	@PostMapping("/add")
	public R<String> add(@Valid @RequestBody SoBillAddOrEditRequest soBillAddOrEditRequest) {
		SoHeader soHeader = soHeaderBiz.add(soBillAddOrEditRequest);
		return R.success(String.format("新增出库单成功，出库单编码：%s", soHeader.getSoBillNo()));
	}

	/**
	 * 发货单：删除
	 */
	@ApiLog("出库单管理-删除")
	@PostMapping("/remove")
	public R<String> remove(@Valid @RequestBody SoBillRemoveRequest soBillRemoveRequest) {
		boolean isRemoveSuccess = soHeaderBiz.remove(soBillRemoveRequest.getIdList());
		return R.success(isRemoveSuccess ? "删除成功" : "删除失败，请稍后再试");
	}

	/**
	 * 发货单编辑：根据发货单查找发货单信息
	 */
	@PostMapping("/detailByEdit")
	public R<SoBillEditResponse> detailByEdit(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		return R.data(soHeaderBiz.findSoBillByEdit(soBillIdRequest.getSoBillId()));
	}

	/**
	 * 发货单：编辑
	 */
	@ApiLog("出库单管理-编辑")
	@PostMapping("/edit")
	public R<String> edit(@Validated(Update.class) @RequestBody SoBillAddOrEditRequest soBillAddOrEditRequest) {
		SoHeader soHeader = soHeaderBiz.edit(soBillAddOrEditRequest);
		return R.success(String.format("编辑出库单成功，出库单编码：%s", soHeader.getSoBillNo()));
	}

	/**
	 * 发货单明细：根据发货单id查找发货单头表信息
	 */
	@PostMapping("/detail_header")
	public R<SoHeaderForDetailResponse> headerForDetail(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		return R.data(soHeaderBiz.findSoHeaderForDetailBySoBillId(soBillIdRequest.getSoBillId()));
	}

	/**
	 * 发货单明细：根据发货单id查找发货明细信息
	 */
	@PostMapping("/detail_detail")
	public R<Page<SoDetailForDetailResponse>> detailForDetail(Query query,
															  @Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		Page<SoDetailForDetailResponse> pageSoDetail = soDetailBiz.pageSoDetailForDetailBySoBillId(query, soBillIdRequest);
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
	public R<Page<LogForSoDetailResponse>> logForSoDetail(Query query,
														  @Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		Page<LogForSoDetailResponse> pageLog = soHeaderBiz.pageLogById(Condition.getPage(query), soBillIdRequest.getSoBillId());
		return R.data(pageLog);
	}

	/**
	 * 发货单导出：服务端导出
	 */
	@PostMapping("/export")
	public void export(@RequestBody SoHeaderPageQuery soHeaderPageQuery, HttpServletResponse response) {
		soHeaderBiz.export(soHeaderPageQuery, response);
	}

	/**
	 * 发货单关闭：根据发货单id关闭发货单
	 */
	@PostMapping("/close")
	public R<String> close(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		soHeaderBiz.closeById(soBillIdRequest.getSoBillId());
		return R.success("关闭成功");
	}

	/**
	 * 未发货记录：分页查找
	 */
	@PostMapping("/pageNotSoPick")
	public R<IPage<NotSoPickPageResponse>> pageNotLogSoPick(
		Query query, @RequestBody NotSoPickPageQuery notSoPickPageQuery) {
		return R.data(soDetailBiz.pageNotSoPick(query, notSoPickPageQuery));
	}

	/**
	 * 未发货记录：服务端导出
	 */
	@PostMapping("/exportNotSoPick")
	public void exportNotSoPick(
		@RequestBody NotSoPickPageQuery notSoPickPageQuery, HttpServletResponse response) {
		soDetailBiz.exportNotSoPick(notSoPickPageQuery, response);
	}

	/**
	 * PC拣货：获取发货单头表信息
	 */
	@PostMapping("/getSoHeaderByPickPc")
	public R<PickByPcSoHeaderResponse> getSoHeaderByPickPc(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		return R.data(soHeaderBiz.getSoHeaderByPickPc(soBillIdRequest));
	}

	/**
	 * PC拣货：获取出库但可用拣货的明细简要信息
	 */
	@GetMapping("/getLineNoAndSkuSelectList")
	public R<List<LineNoAndSkuSelectResponse>> getLineNoAndSkuSelectList(Long soBillId) {
		return R.data(soDetailBiz.getLineNoAndSkuSelectList(soBillId));
	}

	/**
	 * 分配：获取分配页面发货单头表和明细信息
	 */
	@PostMapping("/getSoBillDataByDistribution")
	public R<SoBillDistributedResponse> getSoBillDataByDistribution(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		return R.data(soHeaderBiz.findSoBillForDistBySoBillId(soBillIdRequest.getSoBillId()));
	}

	/**
	 * PC拣货：根据明细行号获取明细和对应的可用库存
	 */
	@PostMapping("/getSoDetailAndStock")
	public R<SoDetailAndStockResponse> getSoDetailAndStock(@Valid @RequestBody
														   SoDetailAndStockRequest soDetailAndStockRequest) {
		return R.data(soHeaderBiz.getSoDetailAndStock(soDetailAndStockRequest));
	}

	/**
	 * 分配：根据发货单id获取拣货计划信息
	 */
	@PostMapping("/getSoPickPlan")
	public R<List<SoPickPlanForDistResponse>> getSoPickPlan(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		List<SoPickPlanForDistResponse> soPickPlanList = new LinkedList<>();
		for (int i = 0; i < 10; i++) {
			SoPickPlanForDistResponse soPickPlan = new SoPickPlanForDistResponse();
			soPickPlan.setPickPlanId(ConvertUtil.convert(1000 + i, Long.class));
			soPickPlan.setPickPlanQty(ConvertUtil.convert(1000 + i, BigDecimal.class));
			if (i % 2 == 0 || i % 3 == 0) {
				soPickPlan.setBoxCode("box1");
				soPickPlan.setZoneId(10L);
				soPickPlan.setZoneName("zone1");
				soPickPlan.setLocId(100L);
				soPickPlan.setLocName("loc1");
				soPickPlan.setLpnCode("lpn1");
			} else {
				soPickPlan.setBoxCode("box2");
				soPickPlan.setZoneId(20L);
				soPickPlan.setZoneName("zone2");
				soPickPlan.setLocId(200L);
				soPickPlan.setLocName("loc2");
				soPickPlan.setLpnCode("lpn2");
			}
			soPickPlan.setSkuCode("sku" + i);
			soPickPlan.setLotNumber("lotNumber" + 1);
			soPickPlan.setEnableQty(ConvertUtil.convert(8000 + i, BigDecimal.class));
			soPickPlan.setSurplusQty(ConvertUtil.convert(10000 + i, BigDecimal.class));
			soPickPlan.setState(SoBillStateEnum.ALLOCATED);

			soPickPlan.setSkuLot1("skuLot1");
			soPickPlan.setSkuLot2("skuLot2");
			soPickPlan.setSkuLot3("skuLot3");
			soPickPlan.setSkuLot4("skuLot4");
			soPickPlan.setSkuLot5("skuLot5");
			soPickPlan.setSkuLot6("skuLot6");
			soPickPlan.setSkuLot7("skuLot7");
			soPickPlan.setSkuLot8("skuLot8");
			soPickPlanList.add(soPickPlan);
		}
		return R.data(soPickPlanList);
	}

	/**
	 * 分配：自动分配
	 */
	@PostMapping("/automaticAssign")
	public R<String> automaticAssign(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		return null;
	}

	/**
	 * 分配：取消分配
	 */
	@PostMapping("/cancelAll")
	public R<String> cancelAll(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		return null;
	}

	/**
	 * 分配：确认下发
	 */
	@PostMapping("/issued")
	public R<String> issued(@Valid @RequestBody SoBillIdRequest soBillIdRequest) {
		return null;
	}

	/**
	 * 分配调整：根据物品id查找物品可分配库存信息
	 */
	@PostMapping("/getEnableStockBySkuId")
	public R<List<StockSoPickPlanResponse>> getEnableStockBySkuId(@Valid @RequestBody SkuIdRequest skuIdRequest) {
		List<StockSoPickPlanResponse> stockDistributedList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			StockSoPickPlanResponse stock = new StockSoPickPlanResponse();
			stock.setStockId(ConvertUtil.convert(i, Long.class));
			stock.setBoxCode("boxTest");
			stock.setLocId(ConvertUtil.convert(123456,Long.class));
			stock.setLocName("库位test");
			stock.setZoneId(ConvertUtil.convert(654321,Long.class));
			stock.setZoneName("库区test");
			stock.setSkuCode("HZ916");
			stock.setLotNumber("lotNumber" + i);
			stock.setStockEnableQty(ConvertUtil.convert(50 + i, BigDecimal.class));
			stock.setStockBalanceQty(ConvertUtil.convert(50 - i, BigDecimal.class));
			stock.setLpnCode("lpnTest");
			stock.setStockState("状态test");
			stock.setSkuLot1("skuLot1");
			stock.setSkuLot2("skuLot2");
			stock.setSkuLot3("skuLot3");
			stock.setSkuLot4("skuLot4");
			stock.setSkuLot5("skuLot5");
			stock.setSkuLot6("skuLot6");
			stock.setSkuLot7("skuLot7");
			stock.setSkuLot8("skuLot8");
			stockDistributedList.add(stock);
		}
		return R.data(stockDistributedList);
	}

	/**
	 * 分配调整：保存分配信息、更新库存占用数量
	 */
	@PostMapping("/saveAssign")
	public R<String> saveAssign(@Valid @RequestBody SoBillDistributedRequest soBillDistributedRequest) {
		System.out.println(soBillDistributedRequest.getSoBillId());
		System.out.println(soBillDistributedRequest.getSoDetailId());
		System.out.println(soBillDistributedRequest.getStockIdAndSoPickPlanQtyList());
		return null;
	}
}
