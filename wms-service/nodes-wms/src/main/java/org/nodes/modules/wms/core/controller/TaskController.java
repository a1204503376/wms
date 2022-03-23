/*
 *      Copyright (c) 2018-2028, Nodes All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Nodes
 */
package org.nodes.modules.wms.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.modules.wms.core.service.ITaskService;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.vo.TaskVO;
import org.nodes.wms.core.system.wrapper.TaskWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 控制器
 *
 * @author pengwei
 * @since 2019-12-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/core/task")
@Api(value = "任务", tags = "任务接口")
public class TaskController extends BladeController {

	private ITaskService taskService;

	/**
	 * 详情
	 */
	@ApiLog("任务接口-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入task")
	public R<TaskVO> detail(@ApiParam(value = "任务ID", required = true) @RequestParam Long taskId) {
		return R.data(taskService.getDetail(taskId));
	}

//	/**
//	 * 获取任务集合
//	 */
//	@ApiLog("任务接口-获取任务集合")
//	@GetMapping("/list")
//	@ApiOperation(value = "获取任务集合", notes = "传入task查询条件")
//	public R<List<TaskVO>> list(Task task) {
//		QueryWrapper<Task> queryWrapper = Condition.getQueryWrapper(task);
//		return R.data(taskService.list(queryWrapper));
//	}

	/**
	 * 自定义分页
	 */
	@ApiLog("任务接口-自定义分页")
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入task")
	public R<IPage<TaskVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<Task> pages = taskService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Task.class)
			.lambda()
			.orderByDesc(Task::getTaskId));
		return R.data(TaskWrapper.build().pageVO(pages));
	}

	@ApiLog("任务接口-变更执行任务人员")
	@PostMapping("/changeUser")
	@ApiOperation(value = "变更执行任务人员", notes = "传入task")
	public R changUser(@ApiParam(value = "主键集合", required = true) @RequestParam String ids,
					   Long userId) {
		return R.status(taskService.changeUser(ids, userId));
	}

	@ApiLog("任务接口-关闭任务")
	@PostMapping("/close")
	@ApiOperation(value = "关闭任务", notes = "传入task")
	public R close(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(taskService.close(ids));
	}

	@ApiLog("任务接口-查询任务数据根据类型")
	@PostMapping("/getTaskListForType")
	@ApiOperation(value = "查询任务数据根据类型", notes = "传入task")
	public List<Task> getTaskListForType(@RequestParam Task task) {
		return taskService.getTaskListForType(task);
	}
}
