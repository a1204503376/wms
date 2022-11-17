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
package org.nodes.modules.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.base.entity.AuthClient;
import org.nodes.core.base.service.IAuthClientService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;

/**
 *  应用管理控制器
 *
 * @author Nodes
 */
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_SYSTEM_NAME + "/client")
@Api(value = "应用管理", tags = "应用管理")
public class AuthClientController extends BladeController {

	private IAuthClientService clientService;

	/**
	* 详情
	*/
	@ApiLog("应用管理-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入client")
	public R<AuthClient> detail(AuthClient authClient) {
		AuthClient detail = clientService.getOne(Condition.getQueryWrapper(authClient));
		return R.data(detail);
	}

	/**
	* 分页
	*/
	@GetMapping("/list")
	@ApiOperation(value = "分页", notes = "传入client")
	public R<IPage<AuthClient>> list(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<AuthClient> pages = clientService.page(Condition.getPage(query), Condition.getQueryWrapper(params, AuthClient.class));
		return R.data(pages);
	}

	/**
	* 新增
	*/
	@ApiLog("应用管理-新增")
	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入client")
	public R save(@Valid @RequestBody AuthClient authClient) {
		return R.status(clientService.save(authClient));
	}

	/**
	* 修改
	*/
	@ApiLog("应用管理-修改")
	@PostMapping("/update")
	@ApiOperation(value = "修改", notes = "传入client")
	public R update(@Valid @RequestBody AuthClient authClient) {
		return R.status(clientService.updateById(authClient));
	}

	/**
	* 新增或修改
	*/
	@ApiLog("应用管理-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入client")
	public R submit(@Valid @RequestBody AuthClient authClient) {
		return R.status(clientService.saveOrUpdate(authClient));
	}


	/**
	* 删除
	*/
	@ApiLog("应用管理-逻辑删除")
	@PostMapping("/remove")
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(clientService.deleteLogic(Func.toLongList(ids)));
	}


}
