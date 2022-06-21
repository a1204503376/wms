package org.nodes.wms.controller.instock.receive;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.nodes.wms.biz.instock.receiveLog.ReceiveLogBiz;
import org.nodes.wms.dao.application.dto.output.ReceiveBillStateResponse;
import org.nodes.wms.dao.instock.receive.dto.input.*;
import org.nodes.wms.dao.instock.receive.dto.output.EditReceiveResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveResponse;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receive.enums.ReceiveBillStateEnum;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
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
	private final ReceiveLogBiz receiveLogBiz;

	/**
	 * 收货管理分页查询
	 */
	@PostMapping("/page")
	public R<IPage<ReceiveHeaderResponse>> page(@Valid @RequestBody ReceivePageQuery receivePageQuery, Query query) {
		IPage<ReceiveHeaderResponse> pages = receiveBiz.getPage(receivePageQuery, query);
		return R.data(pages);
	}

	/**
	 * 收货管理新建
	 */
	@ApiLog("收货管理-新增")
	@PostMapping("/newReceive")
	public R<String> newReceive(@Valid @RequestBody NewReceiveRequest newReceiveRequest) {
		ReceiveHeader receiveHeader = receiveBiz.newReceive(newReceiveRequest);
		return R.success("单号:"+receiveHeader.getReceiveNo()+"保存成功");
	}

	/**
	 * 收货单管理修改
	 */
	@ApiLog("收货管理-修改")
	@PostMapping("/editReceive")
	public R<String> editReceive(@Valid @RequestBody EditReceiveRequest editReceiveRequest) {
		String receiveNo  = receiveBiz.editReceive(editReceiveRequest);
		return R.success("单号:"+receiveNo+"修改成功");
	}

	/**
	 * 收货管理删除
	 */
	@ApiLog("收货管理-逻辑删除")
	@PostMapping("/delete")
	public R<String> delete(@Valid @RequestBody DeleteReceiveIdRequest deleteReceiveIdRequest) {
		if(receiveBiz.remove(deleteReceiveIdRequest.getReceiveIdList())){
			return R.success("删除成功");
		}
		return R.fail("删除失败");
	}

	/**
	 * 查看收货单明细
	 */
	@PostMapping("/getReceiveDetailById")
	public R<ReceiveResponse> getReceiveDetailById(@Valid @RequestBody ReceiveIdRequest receiveIdRequest) {
		return R.data(receiveBiz.getReceiveDetail(receiveIdRequest.getReceiveId()));
	}
	/**
	 * 编辑页面数据回显
	 */
	@PostMapping("/getEditReceiveById")
	public R<EditReceiveResponse> getEditReceiveById(@Valid @RequestBody ReceiveIdRequest receiveIdRequest) {
		return R.data(receiveBiz.getEditReceiveResponse(receiveIdRequest.getReceiveId()));
	}


	/**
	 * 关闭收货单
	 */
	@ApiLog("收货管理-修改状态")
	@PostMapping("/editBillState")
	public R<Boolean> editBillState(@Valid @RequestBody ReceiveIdRequest headerIdRequest) {
		return R.status(receiveBiz.editBillState(headerIdRequest.getReceiveId()));
	}

	/**
	 * 导出
	 */
	@PostMapping("export")
	public void export(@RequestBody ReceivePageQuery receivePageQuery, HttpServletResponse response) {
		receiveBiz.exportExcel(receivePageQuery, response);
	}
	/**
	 * 获取收货单状态集合
	 */
	@GetMapping("getReceiveStateList")
	public R<List<ReceiveBillStateResponse>> getReceiveStateList() {
		return R.data(ReceiveBillStateEnum.getList());
	}

	/**
	 * 根据收货单id获取清点记录集合
	 * @param receiveId 收货单id
	 */
	@GetMapping("/getReceiveLogList")
	public R<List<ReceiveLogResponse>> getReceiveList(Long receiveId){
		return R.data(receiveLogBiz.getReceiveLogList(receiveId));
   }
}
