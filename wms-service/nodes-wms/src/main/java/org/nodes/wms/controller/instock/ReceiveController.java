package org.nodes.wms.controller.instock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.instock.InStockBiz;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.nodes.wms.biz.instock.receiveLog.ReceiveLogBiz;
import org.nodes.wms.dao.application.dto.output.ReceiveBillStateResponse;
import org.nodes.wms.dao.common.log.dto.output.LogReceiveResponse;
import org.nodes.wms.dao.instock.receive.dto.input.*;
import org.nodes.wms.dao.instock.receive.dto.output.*;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receive.enums.ReceiveHeaderStateEnum;
import org.nodes.wms.dao.instock.receiveLog.dto.input.ReceiveCancelRequest;
import org.nodes.wms.dao.instock.receiveLog.dto.input.ReceiveLogPageQuery;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogPageResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;
import org.nodes.wms.dao.stock.dto.output.SerialSelectResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


/**
 * 收货管理API
 *
 * @author nodesc
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "receive")
@Api(value = "收货接口", tags = "收货接口")
public class ReceiveController {
	private final ReceiveBiz receiveBiz;
	private final ReceiveLogBiz receiveLogBiz;
	private final InStockBiz inStockBiz;

	/**
	 * 收货管理:分页查询
	 */
	@ApiOperation(value = "收货单分页查询", notes = "传入receivePageQuery、query")
	@PostMapping("/page")
	public R<IPage<ReceiveHeaderResponse>> page(@Valid @RequestBody ReceivePageQuery receivePageQuery, Query query) {
		IPage<ReceiveHeaderResponse> pages = receiveBiz.getPage(receivePageQuery, query);
		return R.data(pages);
	}

	/**
	 * 收货管理:新建
	 */
	@ApiLog("收货管理-新增")
	@ApiOperation(value = "收货单新增", notes = "传入newReceiveRequest")
	@PostMapping("/newReceive")
	public R<String> newReceive(@Valid @RequestBody NewReceiveRequest newReceiveRequest) {
		ReceiveHeader receiveHeader = receiveBiz.newReceive(newReceiveRequest);
		return R.success("单号:" + receiveHeader.getReceiveNo() + "保存成功");
	}

	/**
	 * 收货单管理:修改
	 */
	@ApiLog("收货管理-修改")
	@ApiOperation(value = "收货单修改", notes = "传入editReceiveRequest")
	@PostMapping("/editReceive")
	public R<String> editReceive(@Valid @RequestBody EditReceiveRequest editReceiveRequest) {
		String receiveNo = receiveBiz.editReceive(editReceiveRequest);
		return R.success("单号:" + receiveNo + "修改成功");
	}

	/**
	 * 收货管理:删除
	 */
	@ApiLog("收货管理-逻辑删除")
	@ApiOperation(value = "收货单删除")
	@PostMapping("/delete")
	public R<String> delete(@Valid @RequestBody DeleteReceiveIdRequest deleteReceiveIdRequest) {
		if (receiveBiz.remove(deleteReceiveIdRequest.getReceiveIdList())) {
			return R.success("删除成功");
		}
		return R.fail("删除失败");
	}

	/**
	 * 收货管理:查看收货单明细
	 */
	@ApiOperation(value = "收货单查看明细")
	@PostMapping("/getReceiveDetailById")
	public R<ReceiveResponse> getReceiveDetailById(@Valid @RequestBody ReceiveIdRequest receiveIdRequest) {
		return R.data(receiveBiz.getReceiveDetail(receiveIdRequest.getReceiveId()));
	}

	/**
	 * 收货管理:编辑页面数据回显
	 */
	@ApiOperation(value = "收货单编辑时查询")
	@PostMapping("/getEditReceiveById")
	public R<EditReceiveResponse> getEditReceiveById(@Valid @RequestBody ReceiveIdRequest receiveIdRequest) {
		return R.data(receiveBiz.getEditReceiveResponse(receiveIdRequest.getReceiveId()));
	}

	/**
	 * 收货管理:关闭收货单
	 */
	@ApiOperation(value = "收货单查看关闭")
	@ApiLog("收货管理-修改状态")
	@PostMapping("/editBillState")
	public R<Boolean> editBillState(@Valid @RequestBody ReceiveIdRequest headerIdRequest) {
		return R.status(receiveBiz.editBillState(headerIdRequest.getReceiveId()));
	}

	/**
	 * 收货管理:导出
	 */
	@ApiOperation(value = "导出")
	@PostMapping("export")
	public void export(@RequestBody ReceivePageQuery receivePageQuery, HttpServletResponse response) {
		receiveBiz.exportExcel(receivePageQuery, response);
	}

	/**
	 * 收货管理:获取收货单状态集合
	 */
	@ApiOperation(value = "获取所有收货单状态")
	@GetMapping("getReceiveStateList")
	public R<List<ReceiveBillStateResponse>> getReceiveStateList() {
		return R.data(ReceiveHeaderStateEnum.getList());
	}

	/**
	 * 收货管理:根据收货单id获取清点记录集合
	 */
	@ApiOperation(value = "根据收货单id获取清点记录", notes = "传入收货单头表id")
	@GetMapping("/getReceiveLogList")
	public R<List<ReceiveLogResponse>> getReceiveLogList(Long receiveId) {
		return R.data(receiveLogBiz.getReceiveLogList(receiveId));
	}

	/**
	 * 收货管理:获取操作日志
	 */
	@ApiOperation(value = "收货单操作日志", notes = "传入收货单头表id")
	@GetMapping("/getLogList")
	public R<List<LogReceiveResponse>> getLogList(Long receiveId) {
		return R.data(receiveBiz.getLogList(receiveId));
	}

	/**
	 * 收货记录：分页查询
	 */
	@ApiOperation(value = "收货记录分页查询", notes = "传入receiveId")
	@PostMapping("/pageReceiveLog")
	public R<IPage<ReceiveLogPageResponse>> pageReceiveLog(
		Query query, @RequestBody ReceiveLogPageQuery receiveLogPageQuery) {
		return R.data(receiveLogBiz.page(query, receiveLogPageQuery));
	}

	/**
	 * 收货记录：导出
	 */
	@ApiOperation(value = "收货记录导出", notes = "传入receiveLogPageQuery")
	@PostMapping("/exportReceiveLog")
	public void exportReceiveLog(@RequestBody ReceiveLogPageQuery receiveLogPageQuery, HttpServletResponse response) {
		receiveLogBiz.exportExcel(receiveLogPageQuery, response);
	}

	/**
	 * 收货记录：撤销收货
	 */
	@ApiOperation(value = "撤销收货", notes = "传入receiveCancelRequest")
	@PostMapping("/cancelReceive")
	public R<String> revoke(@RequestBody ReceiveCancelRequest receiveCancelRequest) {
		inStockBiz.cancelReceive(receiveCancelRequest.getIdList());
		return R.success("撤销成功");
	}

	/**
	 * 未收货明细：分页查询
	 */
	@ApiOperation(value = "未收货明细分页查询", notes = "传入query、notReceiveDetailPageQuery")
	@PostMapping("/pageNotReceiveDetail")
	public R<IPage<NotReceiveDetailResponse>> pageNotReceiveDetail(
		Query query, @RequestBody NotReceiveDetailPageQuery notReceiveDetailPageQuery) {
		return R.data(receiveBiz.pageNotReceiveDetail(query, notReceiveDetailPageQuery));
	}

	/**
	 * 未收货明细：导出
	 */
	@ApiOperation(value = "未收货明细导出", notes = "传入query、notReceiveDetailPageQuery")
	@PostMapping("/exportNotReceiveDetail")
	public void exportNotReceiveDetail(@RequestBody NotReceiveDetailPageQuery notReceiveDetailPageQuery, HttpServletResponse response) {
		receiveBiz.exportNotReceiveDetail(notReceiveDetailPageQuery, response);
	}

	/**
	 * pc收货:获取头表显示信息
	 */
	@ApiOperation(value = "PC收货获取收货单表头信息", notes = "传入receiveIdRequest")
	@PostMapping("/getReceiveByPc")
	public R<ReceiveByPcResponse> getReceiveByPc(@Valid @RequestBody ReceiveIdRequest receiveIdRequest) {
		return R.data(receiveBiz.getReceiveByPcResponse(receiveIdRequest.getReceiveId()));
	}

	/**
	 * pc:收货获取明细显示信息
	 */
	@ApiOperation(value = "PC收货获取收货单明细信息", notes = "传入receiveByPcQuery")
	@PostMapping("/getReceiveDetailByPc")
	public R<ReceiveDetailByPcResponse> getReceiveDetailByPc(@Valid @RequestBody ReceiveByPcQuery receiveByPcQuery) {
		return R.data(receiveBiz.getReceiveDetailByPcResponse(receiveByPcQuery));
	}

	/**
	 * pc收货
	 */
	@ApiOperation(value = "PC收货", notes = "传入receiveByPcQuery")
	@PostMapping("/receiveByPc")
	public R<String> receiveByPc(@Valid @RequestBody ReceiveByPcRequest request) {
		String receiveNo = inStockBiz.receiveByPc(request);
		return R.success("单号:" + receiveNo + "收货成功");
	}

	/**
	 * pc收货:获取序列号下拉列表集合
	 */
	@ApiOperation(value = "PC收货获取序列号组件下拉数据", notes = "传入receiveByPcQuery")
	@GetMapping("/getSerialListByReceiveDetailId")
	public R<List<SerialSelectResponse>> getSerialListByReceiveDetailId(Long receiveDetailId) {
		List<SerialSelectResponse> serialList = receiveBiz.getSerialListByReceiveDetailId(receiveDetailId);
		return R.data(serialList);
	}
}
