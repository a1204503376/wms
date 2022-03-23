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
import org.nodes.core.base.entity.ApiScope;
import org.nodes.core.base.service.IApiScopeService;
import org.nodes.core.base.vo.ApiScopeVO;
import org.nodes.core.base.wrapper.ApiScopeWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springblade.core.cache.constant.CacheConstant.SYS_CACHE;

/**
 * 接口权限控制器
 *
 * @author NodeX
 */
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_SYSTEM_NAME + "/api-scope")
@Api(value = "接口权限", tags = "接口权限")
public class ApiScopeController extends BladeController {

	private IApiScopeService apiScopeService;

	/**
	 * 详情
	 */
	@ApiLog("接口权限-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入dataScope")
	public R<ApiScope> detail(ApiScope dataScope) {
		ApiScope detail = apiScopeService.getOne(Condition.getQueryWrapper(dataScope));
		return R.data(detail);
	}

	/**
	 * 分页
	 */
	@ApiLog("接口权限-列表分页")
	@GetMapping("/list")
	@ApiOperation(value = "分页", notes = "传入dataScope")
	public R<IPage<ApiScopeVO>> list(ApiScope dataScope, Query query) {
		IPage<ApiScope> pages = apiScopeService.page(Condition.getPage(query), Condition.getQueryWrapper(dataScope));
		return R.data(ApiScopeWrapper.build().pageVO(pages));
	}

	/**
	 * 新增
	 */
	@ApiLog("接口权限-新增")
	@PostMapping("/save")
	@CacheEvict(cacheNames = {SYS_CACHE}, allEntries = true)
	@ApiOperation(value = "新增", notes = "传入dataScope")
	public R save(@Valid @RequestBody ApiScope dataScope) {
		return R.status(apiScopeService.save(dataScope));
	}

	/**
	 * 修改
	 */
	@ApiLog("接口权限-修改")
	@PostMapping("/update")
	@CacheEvict(cacheNames = {SYS_CACHE}, allEntries = true)
	@ApiOperation(value = "修改", notes = "传入dataScope")
	public R update(@Valid @RequestBody ApiScope dataScope) {
		return R.status(apiScopeService.updateById(dataScope));
	}

	/**
	 * 新增或修改
	 */
	@ApiLog("接口权限-新增或修改")
	@PostMapping("/submit")
	@CacheEvict(cacheNames = {SYS_CACHE}, allEntries = true)
	@ApiOperation(value = "新增或修改", notes = "传入dataScope")
	public R submit(@Valid @RequestBody ApiScope dataScope) {
		return R.status(apiScopeService.saveOrUpdate(dataScope));
	}


	/**
	 * 删除
	 */
	@ApiLog("接口权限-删除")
	@PostMapping("/remove")
	@CacheEvict(cacheNames = {SYS_CACHE}, allEntries = true)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(apiScopeService.deleteLogic(Func.toLongList(ids)));
	}

}
