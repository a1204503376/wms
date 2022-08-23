package org.nodes.wms.controller.instock;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
public class ReceiveController {
	private final ReceiveBiz receiveBiz;
	private final ReceiveLogBiz receiveLogBiz;
	private final InStockBiz inStockBiz;

	/**
	 * 收货管理:分页查询
	 */
	@PostMapping("/page")
	public R<IPage<ReceiveHeaderResponse>> page(@Valid @RequestBody ReceivePageQuery receivePageQuery, Query query) {
		IPage<ReceiveHeaderResponse> pages = receiveBiz.getPage(receivePageQuery, query);
		return R.data(pages);
	}

	/**
	 * 收货管理:新建
	 */
	@ApiLog("收货管理-新增")
	@PostMapping("/newReceive")
	public R<String> newReceive(@Valid @RequestBody NewReceiveRequest newReceiveRequest) {
		ReceiveHeader receiveHeader = receiveBiz.newReceive(newReceiveRequest);
		return R.success("单号:" + receiveHeader.getReceiveNo() + "保存成功");
	}

	/**
	 * 收货单管理:修改
	 */
	@ApiLog("收货管理-修改")
	@PostMapping("/editReceive")
	public R<String> editReceive(@Valid @RequestBody EditReceiveRequest editReceiveRequest) {
		String receiveNo = receiveBiz.editReceive(editReceiveRequest);
		return R.success("单号:" + receiveNo + "修改成功");
	}

	/**
	 * 收货管理:删除
	 */
	@ApiLog("收货管理-逻辑删除")
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
	@PostMapping("/getReceiveDetailById")
	public R<ReceiveResponse> getReceiveDetailById(@Valid @RequestBody ReceiveIdRequest receiveIdRequest) {
		return R.data(receiveBiz.getReceiveDetail(receiveIdRequest.getReceiveId()));
	}

	/**
	 * 收货管理:编辑页面数据回显
	 */
	@PostMapping("/getEditReceiveById")
	public R<EditReceiveResponse> getEditReceiveById(@Valid @RequestBody ReceiveIdRequest receiveIdRequest) {
		return R.data(receiveBiz.getEditReceiveResponse(receiveIdRequest.getReceiveId()));
	}

	/**
	 * 收货管理:关闭收货单
	 */
	@ApiLog("收货管理-修改状态")
	@PostMapping("/editBillState")
	public R<Boolean> editBillState(@Valid @RequestBody ReceiveIdRequest headerIdRequest) {
		return R.status(receiveBiz.editBillState(headerIdRequest.getReceiveId()));
	}

	/**
	 * 收货管理:导出
	 */
	@PostMapping("export")
	public void export(@RequestBody ReceivePageQuery receivePageQuery, HttpServletResponse response) {
		receiveBiz.exportExcel(receivePageQuery, response);
	}

	/**
	 * 收货管理:获取收货单状态集合
	 */
	@GetMapping("getReceiveStateList")
	public R<List<ReceiveBillStateResponse>> getReceiveStateList() {
		return R.data(ReceiveHeaderStateEnum.getList());
	}

	/**
	 * 收货管理:根据收货单id获取清点记录集合
	 */
	@GetMapping("/getReceiveLogList")
	public R<List<ReceiveLogResponse>> getReceiveLogList(Long receiveId) {
		return R.data(receiveLogBiz.getReceiveLogList(receiveId));
	}

	/**
	 * 收货管理:获取操作日志
	 */
	@GetMapping("/getLogList")
	public R<List<LogReceiveResponse>> getLogList(Long receiveId) {
		return R.data(receiveBiz.getLogList(receiveId));
	}

	/**
	 * 收货记录：分页查询
	 */
	@PostMapping("/pageReceiveLog")
	public R<IPage<ReceiveLogPageResponse>> pageReceiveLog(
		Query query, @RequestBody ReceiveLogPageQuery receiveLogPageQuery) {
		return R.data(receiveLogBiz.page(query, receiveLogPageQuery));
	}

	/**
	 * 收货记录：导出
	 */
	@PostMapping("/exportReceiveLog")
	public void exportReceiveLog(@RequestBody ReceiveLogPageQuery receiveLogPageQuery, HttpServletResponse response) {
		receiveLogBiz.exportExcel(receiveLogPageQuery, response);
	}

	/**
	 * 收货记录：撤销收货
	 */
	@PostMapping("/cancelReceive")
	public R<String> revoke(@RequestBody ReceiveCancelRequest receiveCancelRequest) {
		inStockBiz.cancelReceive(receiveCancelRequest.getIdList());
		return R.success("撤销成功");
	}

	/**
	 * 未收货明细：分页查询
	 */
	@PostMapping("/pageNotReceiveDetail")
	public R<IPage<NotReceiveDetailResponse>> pageNotReceiveDetail(
		Query query, @RequestBody NotReceiveDetailPageQuery notReceiveDetailPageQuery) {
		return R.data(receiveBiz.pageNotReceiveDetail(query, notReceiveDetailPageQuery));
	}

	/**
	 * 未收货明细：导出
	 */
	@PostMapping("/exportNotReceiveDetail")
	public void exportNotReceiveDetail(
		@RequestBody NotReceiveDetailPageQuery notReceiveDetailPageQuery,
		HttpServletResponse response) {
		receiveBiz.exportNotReceiveDetail(notReceiveDetailPageQuery, response);
	}

	/**
	 * pc收货:获取头表显示信息
	 */
	@PostMapping("/getReceiveByPc")
	public R<ReceiveByPcResponse> getReceiveByPc(@Valid @RequestBody ReceiveIdRequest receiveIdRequest) {
		return R.data(receiveBiz.getReceiveByPcResponse(receiveIdRequest.getReceiveId()));
	}

	/**
	 * pc:收货获取明细显示信息
	 */
	@PostMapping("/getReceiveDetailByPc")
	public R<ReceiveDetailByPcResponse> getReceiveDetailByPc(@Valid @RequestBody ReceiveByPcQuery receiveByPcQuery) {
		return R.data(receiveBiz.getReceiveDetailByPcResponse(receiveByPcQuery));
	}

	/**
	 * pc收货
	 */
	@PostMapping("/receiveByPc")
	public R<String> receiveByPc(@Valid @RequestBody ReceiveByPcRequest request) {
		String receiveNo = inStockBiz.receiveByPc(request);
		return R.success("单号:" + receiveNo + "收货成功");
	}
}
