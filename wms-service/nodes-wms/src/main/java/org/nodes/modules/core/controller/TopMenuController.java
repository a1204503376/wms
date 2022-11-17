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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.base.entity.TopMenu;
import org.nodes.core.base.service.ITopMenuService;
import org.nodes.core.base.vo.GrantVO;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;

import static org.springblade.core.cache.constant.CacheConstant.SYS_CACHE;

/**
 * 顶部菜单表 控制器
 *
 * @author NodeX
 * @since 2019-07-14
 */
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_SYSTEM_NAME + "/topmenu")
@Api(value = "顶部菜单表", tags = "顶部菜单")
public class TopMenuController extends BladeController {

	private ITopMenuService topMenuService;

	/**
	 * 详情
	 */
	@ApiLog("顶部菜单-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入topMenu")
	public R<TopMenu> detail(TopMenu topMenu) {
		TopMenu detail = topMenuService.getOne(Condition.getQueryWrapper(topMenu));
		return R.data(detail);
	}

	/**
	 * 分页 顶部菜单表
	 */
	@GetMapping("/list")
	@ApiOperation(value = "分页", notes = "传入topMenu")
	public R<IPage<TopMenu>> list(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		LambdaQueryWrapper queryWrapper = Condition.getQueryWrapper(params, TopMenu.class)
			.lambda()
			.orderByAsc(TopMenu::getSort);
		IPage<TopMenu> pages = topMenuService.page(Condition.getPage(query), queryWrapper);
		return R.data(pages);
	}

	/**
	 * 新增 顶部菜单表
	 */
	@ApiLog("顶部菜单-新增")
	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入topMenu")
	public R save(@Valid @RequestBody TopMenu topMenu) {
		return R.status(topMenuService.save(topMenu));
	}

	/**
	 * 修改 顶部菜单表
	 */
	@ApiLog("顶部菜单-修改")
	@PostMapping("/update")
	@ApiOperation(value = "修改", notes = "传入topMenu")
	public R update(@Valid @RequestBody TopMenu topMenu) {
		return R.status(topMenuService.updateById(topMenu));
	}

	/**
	 * 新增或修改 顶部菜单表
	 */
	@ApiLog("顶部菜单-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入topMenu")
	public R submit(@Valid @RequestBody TopMenu topMenu) {
		return R.status(topMenuService.saveOrUpdate(topMenu));
	}


	/**
	 * 删除 顶部菜单表
	 */
	@ApiLog("顶部菜单-逻辑删除")
	@PostMapping("/remove")
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(topMenuService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 设置顶部菜单
	 */
	@ApiLog("顶部菜单-顶部菜单配置")
	@PostMapping("/grant")
	@ApiOperation(value = "顶部菜单配置", notes = "传入topMenuId集合以及menuId集合")
	@CacheEvict(cacheNames = {SYS_CACHE}, allEntries = true)
	public R grant(@RequestBody GrantVO grantVO) {
		boolean temp = topMenuService.grant(grantVO.getTopMenuIds(), grantVO.getMenuIds());
		return R.status(temp);
	}

}
