package org.nodes.wms.controller.scheduling;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.dao.task.dto.QueryAndFrozenEnableOutboundRequest;
import org.nodes.wms.dao.task.dto.SyncTaskStateRequest;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.SCHEDULING_SYSTEM_API)
public class SchedulingController {

	/**
	 * agv执行状态同步
	 *
	 * @param request 请求参数
	 * @return 消息
	 */
	@PostMapping("syncTaskState")
	public R<String> syncTaskState(@Valid @RequestBody
									   SyncTaskStateRequest request) {
		return R.data("TODO");
	}

	/**
	 * 查询可用的出库接驳区库位，并冻结
	 *
	 * @param request 请求参数
	 * @return 可用的库位编码
	 */
	@PostMapping("queryAndFrozenEnableOutbound")
	public R<String> queryAndFrozenEnableOutbound(@Valid @RequestBody
													  QueryAndFrozenEnableOutboundRequest request) {
		return R.data("TODO");
	}
}
