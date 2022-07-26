package org.nodes.wms.controller.task;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.task.TaskBiz;
import org.nodes.wms.dao.task.dto.input.AgainIssuedlTask;
import org.nodes.wms.dao.task.dto.input.CancelTaskRequest;
import org.nodes.wms.dao.task.dto.input.StopTaskRequest;
import org.nodes.wms.dao.task.dto.input.TaskDetailPageRequest;
import org.nodes.wms.dao.task.dto.output.TaskDetailExcelResponse;
import org.nodes.wms.dao.task.dto.output.TaskDetailPageResponse;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "taskDetail")
public class TaskDetailController {
	private final TaskBiz taskBiz;


	@PostMapping("/page")
	@ApiLog("任务详情-分页查询任务详情")
	public R<IPage<TaskDetailPageResponse>> page(Query query, @RequestBody TaskDetailPageRequest request) {
		return R.data(taskBiz.selectPage(request, query));
	}

	@PostMapping("/export")
	@ApiLog("任务详情-导出任务详情")
	public void export(@ApiIgnore @RequestBody HashMap<String, Object> params, HttpServletResponse response) {
		List<TaskDetailExcelResponse> responseList = taskBiz.selectTaskDetailList(params);
		responseList.forEach(item -> {
			item.setTypeCdValue(item.getTypeCd().getDesc());
			item.setProcTypeValue(item.getProcType().getDesc());
			item.setTaskStateValue(item.getTaskState().getDesc());
			item.setTaskDetailStatusValue(item.getTaskDetailStatus().getDesc());
			item.setStockStatusValue(item.getStockStatus().getDesc());
		});
		ExcelUtil.export(response, "任务详情", "任务详情数据表", responseList, TaskDetailExcelResponse.class);
	}

	@PostMapping("/stopTask")
	@ApiLog("任务详情-停止任务")
	public void stopTask(StopTaskRequest request) {
		taskBiz.stop(request);
	}

	@PostMapping("/cancelTask")
	@ApiLog("任务详情-取消任务")
	public void cancelTask(CancelTaskRequest request) {
		taskBiz.cancel(request);
	}

	@PostMapping("againIssuedlTask")
	@ApiLog("任务详情-重新下发")
	public void againIssuedlTask(AgainIssuedlTask request) {
		taskBiz.restart(request);
	}


}
