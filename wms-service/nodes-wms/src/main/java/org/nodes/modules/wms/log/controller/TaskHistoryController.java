package org.nodes.modules.wms.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.system.entity.TaskHistory;
import org.nodes.wms.core.system.service.ITaskHistoryService;
import org.nodes.wms.core.system.vo.TaskHistoryVO;
import org.nodes.wms.core.system.wrapper.TaskHistoryWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * 任务履历 控制器
 *
 * @author NodeX
 * @since 2020-06-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/log/taskHistory")
@Api(value = "任务履历", tags = "任务履历接口")
public class TaskHistoryController extends BladeController {

	private ITaskHistoryService taskHistoryService;


	/**
	 * 任务履历分页
	 */
	@ApiLog("任务履历接口-任务履历分页")
	@GetMapping("/page")
	@ApiOperation(value = "任务履历分页", notes = "传入taskHistory")
	public R<IPage<TaskHistoryVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<TaskHistory> pages = taskHistoryService.page(Condition.getPage(query), Condition.getQueryWrapper(params, TaskHistory.class)
			.lambda().orderByDesc(TaskHistory::getTaskHistoryId));
		return R.data(TaskHistoryWrapper.build().pageVO(pages));
	}


}
