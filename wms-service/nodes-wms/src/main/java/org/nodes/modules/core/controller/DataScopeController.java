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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.base.dto.DataScopeDTO;
import org.nodes.core.base.entity.DataScope;
import org.nodes.core.base.service.IDataScopeService;
import org.nodes.core.base.vo.DataScopeVO;
import org.nodes.core.base.wrapper.DataScopeWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springblade.core.cache.constant.CacheConstant.SYS_CACHE;

/**
 * 数据权限控制器
 *
 * @author NodeX
 */
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_SYSTEM_NAME + "/data-scope")
@Api(value = "数据权限", tags = "数据权限")
public class DataScopeController extends BladeController {

	private IDataScopeService dataScopeService;

	/**
	 * 详情
	 */
	@ApiLog("数据权限-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入dataScope")
	public R<DataScope> detail(DataScope dataScope) {
		DataScope detail = dataScopeService.getOne(Condition.getQueryWrapper(dataScope));
		return R.data(detail);
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperation(value = "分页", notes = "传入dataScope")
	public R<List<DataScopeVO>> list(DataScope dataScope) {
		List<DataScope> list = dataScopeService.list(Condition.getQueryWrapper(dataScope));
		return R.data(DataScopeWrapper.build().listVO(list));
	}

	/**
	 * 新增
	 */
	@ApiLog("数据权限-新增")
	@PostMapping("/save")
	@CacheEvict(cacheNames = {SYS_CACHE}, allEntries = true)
	@ApiOperation(value = "新增", notes = "传入dataScope")
	public R save(@Valid @RequestBody DataScope dataScope) {
		CacheUtil.clear(SYS_CACHE);
		return R.status(dataScopeService.save(dataScope));
	}

	/**
	 * 修改
	 */
	@ApiLog("数据权限-修改")
	@PostMapping("/update")
	@CacheEvict(cacheNames = {SYS_CACHE}, allEntries = true)
	@ApiOperation(value = "修改", notes = "传入dataScope")
	public R update(@Valid @RequestBody DataScope dataScope) {
		CacheUtil.clear(SYS_CACHE);
		return R.status(dataScopeService.updateById(dataScope));
	}

	/**
	 * 新增或修改
	 */
	@ApiLog("数据权限-新增或修改")
	@PostMapping("/submit")
	@CacheEvict(cacheNames = {SYS_CACHE}, allEntries = true)
	@ApiOperation(value = "新增或修改", notes = "传入dataScope")
	public R submit(@Valid @RequestBody DataScopeDTO dataScope) {
		CacheUtil.clear(SYS_CACHE);
		return R.status(dataScopeService.submit(dataScope));
	}


	/**
	 * 删除
	 */
	@ApiLog("数据权限-删除")
	@PostMapping("/remove")
	@CacheEvict(cacheNames = {SYS_CACHE}, allEntries = true)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CacheUtil.clear(SYS_CACHE);
		return R.status(dataScopeService.deleteLogic(Func.toLongList(ids)));
	}
}
