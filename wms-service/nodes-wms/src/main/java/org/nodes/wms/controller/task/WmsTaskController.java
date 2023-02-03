package org.nodes.wms.controller.task;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.task.WmsTaskBiz;
import org.nodes.wms.dao.task.dto.input.CancelTaskRequest;
import org.nodes.wms.dao.task.dto.input.ContinueTaskRequest;
import org.nodes.wms.dao.task.dto.input.StopTaskRequest;
import org.nodes.wms.dao.task.dto.input.TaskPageQuery;
import org.nodes.wms.dao.task.dto.output.TaskPageResponse;
import org.nodes.wms.dao.task.dto.output.TaskStateSelectResponse;
import org.nodes.wms.dao.task.dto.output.TaskTypeSelectResponse;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.nodes.wms.dao.task.enums.WmsTaskTypeEnum;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "taskDetail")
@Api(value = "工作任务", tags = "工作任务接口")
public class WmsTaskController {
	private final WmsTaskBiz wmsTaskBiz;

	/**
	 * 库内管理:获取工作任务分页
	 *
	 * @param query         分页参数
	 * @param taskPageQuery 查询参数
	 * @return 分页对象
	 */
	@ApiOperation(value = "分页")
	@PostMapping("/page")
	public R<IPage<TaskPageResponse>> page(Query query, @RequestBody TaskPageQuery taskPageQuery) {
		return R.data(wmsTaskBiz.page(taskPageQuery, query));
	}

	/**
	 * 库内管理-工作任务:获取任务类型下拉框返回集合
	 */
	@ApiOperation(value = "任务类型组件数据")
	@PostMapping("getTaskTypeSelectResponse")
	public R<List<TaskTypeSelectResponse>> getTaskTypeSelectResponse() {
		return R.data(WmsTaskTypeEnum.getList());
	}

	/**
	 * 库内管理-工作任务:获取任务状态下拉框返回集合
	 */
	@ApiOperation(value = "任务状态组件数据")
	@PostMapping("getTaskStateSelectResponse")
	public R<List<TaskStateSelectResponse>> getTaskStateSelectResponse() {
		return R.data(WmsTaskStateEnum.getList());
	}

	/**
	 * 库内管理-工作任务:导出
	 */
	@ApiOperation(value = "导出")
	@PostMapping("/export")
	public void export(@RequestBody TaskPageQuery taskPageQuery, HttpServletResponse response) {
		wmsTaskBiz.export(taskPageQuery, response);
	}

	/**
	 * 停止任务
	 */
	@ApiOperation(value = "停止任务")
	@PostMapping("/stopTask")
	@ApiLog("任务详情-停止任务")
	public void stopTask(StopTaskRequest request) {
		wmsTaskBiz.stop(request);
	}

	/**
	 * 取消任务
	 */
	@ApiOperation(value = "取消任务")
	@PostMapping("/cancelTask")
	@ApiLog("任务详情-取消任务")
	public void cancelTask(@RequestBody @Valid CancelTaskRequest request) {
		wmsTaskBiz.cancel(request.getTaskIdList());
	}

	/**
	 * 继续执行
	 */
	@ApiOperation(value = "继续执行任务")
	@PostMapping("/continueTask")
	@ApiLog("任务详情-继续执行")
	public void continueTask(@RequestBody @Valid ContinueTaskRequest request) {
		wmsTaskBiz.continueTask(request.getTaskIdList());
	}
}
