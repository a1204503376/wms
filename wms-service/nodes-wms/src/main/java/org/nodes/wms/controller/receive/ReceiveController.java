package org.nodes.wms.controller.receive;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.receive.header.ReceiveBiz;
import org.nodes.wms.dao.receive.header.dto.input.HeaderPageQuery;
import org.nodes.wms.dao.receive.header.dto.input.HeaderIdRequest;
import org.nodes.wms.dao.receive.header.dto.input.NewReceiveRequest;
import org.nodes.wms.dao.receive.header.dto.output.HeaderDetailResponse;
import org.nodes.wms.dao.receive.header.dto.output.HeaderResponse;
import org.nodes.wms.dao.receive.log.dto.output.LogResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *  收货管理API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL +"header")
public class ReceiveController {
	private  final ReceiveBiz receiveBiz;
	/**
	 * 收货管理分页查询
	 */
	@GetMapping("/page")
	public R<IPage<HeaderResponse>> page(@Valid @RequestParam HeaderPageQuery headerPageQuery, Query query) {
		IPage<HeaderResponse> pages = receiveBiz.getPage(headerPageQuery,query);
		return R.data(pages);
	}
	/**
	 * 收货管理新建
	 */
	@ApiLog("收货管理-新增")
	@PostMapping("/new")
	public R<Boolean> newReceive(@Valid @RequestParam NewReceiveRequest newReceiveRequest) {
         return  R.status(receiveBiz.newReceive(newReceiveRequest));
	}


	/**
	 * 收货管理删除
	 */
	@ApiLog("收货管理-逻辑删除")
	@PostMapping("/delete")
	public R<Boolean> delete(@Valid @RequestParam HeaderIdRequest headerIdRequest) {
		return R.status(receiveBiz.remove(headerIdRequest.getReceiveId()));
	}

	/**
	 * 查看收货单明细
	 */
	@GetMapping("/detail")
	public R<HeaderDetailResponse>  detail(@Valid @RequestParam HeaderIdRequest headerIdRequest) {
		return R.data(receiveBiz.detail(headerIdRequest.getReceiveId()));
	}
	/**
	 * 查看清点记录
	 */
	@GetMapping("/queryLog")
	public R<List<LogResponse>>  queryLog(@Valid @RequestParam HeaderIdRequest headerIdRequest) {
		return R.data(receiveBiz.queryLog(headerIdRequest.getReceiveId()));
	}


	/**
	 * 关闭收货单
	 */
	@ApiLog("收货管理-修改状态")
	@PostMapping("/edit")
	public R<Boolean> edit(@Valid @RequestParam HeaderIdRequest headerIdRequest) {
		return R.status(receiveBiz.edit(headerIdRequest.getReceiveId()));
	}


}
