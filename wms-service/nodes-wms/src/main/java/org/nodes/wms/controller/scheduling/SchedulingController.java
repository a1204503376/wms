package org.nodes.wms.controller.scheduling;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.task.SchedulingBiz;
import org.nodes.wms.dao.task.dto.QueryAndFrozenEnableOutboundRequest;
import org.nodes.wms.dao.task.dto.SchedulingBroadcastNotificationRequest;
import org.nodes.wms.dao.task.dto.SyncTaskStateRequest;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.api.ResultCode;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 天宜定制：提供给调度系统的接口
 *
 * @author nodesc
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.SCHEDULING_SYSTEM_API)
public class SchedulingController {

	private final SchedulingBiz schedulingBiz;
	private final LpnTypeBiz lpnTypeBiz;

	/**
	 * 调度系统:反馈任务执行状态
	 *
	 * @param request 请求参数
	 * @return 消息
	 */
	@ApiLog("调度系统-同步任务执行状态")
	@PostMapping("syncTaskState")
	@ApiOperation(value = "同步任务执行状态")
	public R<String> syncTaskState(@Valid @RequestBody SyncTaskStateRequest request) {
		schedulingBiz.synchronizeTaskStatus(request);
		return R.success("同步任务执行状态成功");
	}

	/**
	 * 调度系统:查询可用的出库接驳区库位，并冻结
	 *
	 * @param request 请求参数
	 * @return 可用的库位编码
	 */
	@ApiLog("调度系统-查询可用接驳区并冻结")
	@PostMapping("queryAndFrozenEnableOutbound")
	@ApiOperation(value = "调度系统查询可用接驳区并冻结")
	public R<String> queryAndFrozenEnableOutbound(@Valid @RequestBody QueryAndFrozenEnableOutboundRequest request) {
		return R.data(schedulingBiz.selectAndFrozenEnableOutbound(request));
	}

	/**
	 * 调度系统：广播通知
	 *
	 * @param request 通知对象集合
	 */
	@PostMapping("broadcastNotification")
	@ApiOperation(value = "调度系统广播通知")
	public R<String> broadcastNotification(@Valid @RequestBody List<SchedulingBroadcastNotificationRequest> request) {
		schedulingBiz.broadcastNotificationActivity(request);
		return R.success(ResultCode.SUCCESS);
	}

	/**
	 * 成品打包程序: 生成箱码
	 *
	 * @param lpnTypeCode 容器类别编码
	 * @param skuName     物品名称
	 * @param spec        物品型号
	 * @return
	 */
	@GetMapping("generateBoxCode")
	public R<String> generateBoxCode(@RequestParam("lpnTypeCode") String lpnTypeCode,
									 @RequestParam("skuName") String skuName, @RequestParam("spec") String spec) {
		String boxCode = lpnTypeBiz.generateLpnCode(lpnTypeCode, skuName, spec);
		return R.data(boxCode);
	}

}
