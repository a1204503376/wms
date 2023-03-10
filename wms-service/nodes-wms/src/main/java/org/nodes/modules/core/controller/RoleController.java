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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.base.entity.Role;
import org.nodes.core.base.entity.User;
import org.nodes.core.base.service.IUserService;
import org.nodes.core.base.vo.GrantVO;
import org.nodes.core.base.vo.RoleVO;
import org.nodes.core.base.wrapper.RoleWrapper;
import org.nodes.modules.core.service.impl.RoleService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static org.springblade.core.cache.constant.CacheConstant.SYS_CACHE;

/**
 * ?????????
 *
 * @author Nodes
 */
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_SYSTEM_NAME + "/role")
@Api(value = "??????", tags = "??????")
public class RoleController extends BladeController {

	private RoleService roleService;

	private IUserService userService;

	/**
	 * ??????
	 */
	@ApiLog("??????-??????")
	@GetMapping("/detail")
	@ApiOperation(value = "??????", notes = "??????role")
	public R<RoleVO> detail(Role role) {
		Role detail = (Role) roleService.getOne(Condition.getQueryWrapper(role));
		return R.data(RoleWrapper.build().entityVO(detail));
	}

	/**
	 * ??????
	 */
	@GetMapping("/list")
	@ApiOperation(value = "??????", notes = "??????role")
	public R<List<RoleVO>> list(@ApiIgnore @RequestParam Map<String, Object> params) {
		QueryWrapper<Role> queryWrapper = Condition.getQueryWrapper(params, Role.class)
			.notIn("role_alias", "administrator", "developer");
		List<Role> allList = roleService.list(queryWrapper);
		return R.data(RoleWrapper.build().listVO(allList));
	}

	/**
	 * ????????????????????????
	 */
	@ApiLog("??????-????????????")
	@GetMapping("/tree")
	@ApiOperation(value = "????????????", notes = "????????????")
	public R<List<RoleVO>> tree(String tenantId, BladeUser bladeUser) {
		List<RoleVO> tree = roleService.tree(Func.toStrWithEmpty(tenantId, bladeUser.getTenantId()));
		return R.data(tree);
	}

	/**
	 * ???????????????
	 */
	@ApiLog("??????-???????????????")
	@PostMapping("/submit")
	@ApiOperation(value = "???????????????", notes = "??????role")
	@CacheEvict(cacheNames = {SYS_CACHE}, allEntries = true)
	public R submit(@Valid @RequestBody RoleVO role, BladeUser user) {
		if (Func.isEmpty(role.getId())) {
			role.setTenantId(user.getTenantId());
		}
		CacheUtil.clear(SYS_CACHE);
		return R.status(roleService.submit(role));
	}

	/**
	 * ??????
	 */
	@ApiLog("??????-??????")
	@PostMapping("/remove")
	@ApiOperation(value = "??????", notes = "??????ids")
	@CacheEvict(cacheNames = {SYS_CACHE}, allEntries = true)
	public R remove(@ApiParam(value = "????????????", required = true) @RequestParam String ids) {
		List<Long> Ids = Func.toLongList(ids);
		int roleCount = roleService.count(new LambdaQueryWrapper<Role>().in(Role::getParentId, ids));
		int userCount = userService.count(new LambdaQueryWrapper<User>().in(User::getRoleId, Ids));
		if (roleCount > 0) {
			return R.fail("?????????????????????");
		} else if (userCount > 0) {
			return R.fail("??????????????????");
		} else {
			CacheUtil.clear(SYS_CACHE);
			return R.status(roleService.removeByIds(Ids));
		}
	}

	/**
	 * ??????????????????
	 */
	@ApiLog("??????-????????????")
	@PostMapping("/grant")
	@ApiOperation(value = "????????????", notes = "??????roleId????????????menuId??????")
	@CacheEvict(cacheNames = {SYS_CACHE}, allEntries = true)
	public R grant(@RequestBody GrantVO grantVO) {
		boolean temp = roleService.grant(
			grantVO.getRoleIds(), grantVO.getMenuIds(), grantVO.getDataScopeIds(), grantVO.getApiScopeIds());
		return R.status(temp);
	}

}
