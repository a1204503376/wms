package org.nodes.wms.controller.scheduling;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.task.SchedulingBiz;
import org.nodes.wms.dao.task.dto.QueryAndFrozenEnableOutboundRequest;
import org.nodes.wms.dao.task.dto.SchedulingBroadcastNotificationRequest;
import org.nodes.wms.dao.task.dto.SyncTaskStateRequest;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.api.ResultCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.SCHEDULING_SYSTEM_API)
public class SchedulingController {
	private final SchedulingBiz schedulingBiz;

	/**
	 * 调度系统同步任务执行状态
	 *
	 * @param request 请求参数
	 * @return 消息
	 */
	@ApiLog("调度系统同步任务执行状态")
	@PostMapping("syncTaskState")
	public R<String> syncTaskState(@Valid @RequestBody SyncTaskStateRequest request) {
		return R.data(schedulingBiz.synchronizeTaskStatus(request) ? "同步任务执行状态成功" : "同步任务执行状态失败");
	}

	/**
	 * 查询可用的出库接驳区库位，并冻结
	 *
	 * @param request 请求参数
	 * @return 可用的库位编码
	 */
	@ApiLog("调度系统查询可用接驳区并冻结")
	@PostMapping("queryAndFrozenEnableOutbound")
	public R<String> queryAndFrozenEnableOutbound(@Valid @RequestBody QueryAndFrozenEnableOutboundRequest request) {
		return R.data(schedulingBiz.selectAndFrozenEnableOutbound(request));
	}

	/**
	 * 调度系统广播通知
	 *
	 * @param request 通知对象集合
	 */
	@PostMapping("broadcastNotification")
	public R<String> broadcastNotification(@Valid @RequestBody List<SchedulingBroadcastNotificationRequest> request) {
		schedulingBiz.broadcastNotificationActivity(request);
		return R.success(ResultCode.SUCCESS);
	}

}
