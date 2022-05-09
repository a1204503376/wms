package org.nodes.wms.controller.instock.receive;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.instock.receive.header.ReceiveBiz;
import org.nodes.wms.dao.application.dto.output.ReceiveBillStateResponse;
import org.nodes.wms.dao.instock.receive.dto.input.NewReceiveRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveHeaderIdRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveHeaderPageQuery;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveResponse;
import org.nodes.wms.dao.instock.receive.enums.ReceiveBillStateEnum;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


/**
 * 收货管理API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "receive")
public class ReceiveController {
	private final ReceiveBiz receiveBiz;

	/**
	 * 收货管理分页查询
	 */
	@PostMapping("/page")
	public R<IPage<ReceiveHeaderResponse>> page(@Valid @RequestBody ReceiveHeaderPageQuery receiveHeaderPageQuery, Query query) {
		IPage<ReceiveHeaderResponse> pages = receiveBiz.getPage(receiveHeaderPageQuery, query);
		return R.data(pages);
	}

	/**
	 * 收货管理新建
	 */
	@ApiLog("收货管理-新增")
	@PostMapping("/new")
	public R<Boolean> newReceive(@Valid @RequestBody NewReceiveRequest newReceiveRequest) {
		return R.status(receiveBiz.newReceive(newReceiveRequest));
	}


	/**
	 * 收货管理删除
	 */
	@ApiLog("收货管理-逻辑删除")
	@PostMapping("/delete")
	public R<Boolean> delete(@Valid @RequestBody ReceiveHeaderIdRequest headerIdRequest) {
		return R.status(receiveBiz.remove(headerIdRequest.getReceiveId()));
	}

	/**
	 * 查看收货单明细
	 */
	@GetMapping("/detail")
	public R<ReceiveResponse> detail(@Valid @RequestParam ReceiveHeaderIdRequest headerIdRequest) {
		return R.data(receiveBiz.detail(headerIdRequest.getReceiveId()));
	}




	/**
	 * 关闭收货单
	 */
	@ApiLog("收货管理-修改状态")
	@PostMapping("/editBillState")
	public R<Boolean> editBillState(@Valid @RequestBody ReceiveHeaderIdRequest headerIdRequest) {
		return R.status(receiveBiz.editBillState(headerIdRequest.getReceiveId()));
	}

	/**
	 * 导出
	 */
	@PostMapping("export")
	public void export(@RequestBody ReceiveHeaderPageQuery receiveHeaderPageQuery, HttpServletResponse response) {
		receiveBiz.exportExcel(receiveHeaderPageQuery, response);
	}
	/**
	 * 获取收货单状态集合
	 */
	@GetMapping("getReceiveStateList")
	public R<List<ReceiveBillStateResponse>> getReceiveStateList() {
		return R.data(ReceiveBillStateEnum.getList());
	}
}
