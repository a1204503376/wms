package org.nodes.modules.wms.api.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.system.vo.TaskVO;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author pengwei
 * @program WmsCore
 * @description 任务（手持）控制器
 * @create 20200310
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/ApiPDA/taskPDA")
@Api(value = "任务", tags = "任务接口")
public class TaskPDAController {

	private ITaskService taskService;

	@Autowired
	ISoHeaderService soHeaderService;
	/**
	 * 获取任务集合
	 */
	@ApiLog("PDA-获取任务集合")
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "获取任务集合", notes = "传入task查询条件")
	public R<List<TaskVO>> list(Task task) {
		task.setUserId(AuthUtil.getUserId());
		List<TaskVO> list = taskService.list(Condition.getQueryWrapper(task));
		return R.data(list);
	}
	@ApiLog("任务接口-关闭任务")
	@GetMapping("/close")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "关闭任务", notes = "taskId")
	public R close(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(taskService.close(ids));
	}
}
