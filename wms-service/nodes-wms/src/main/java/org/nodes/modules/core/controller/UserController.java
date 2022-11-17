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


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.base.entity.User;
import org.nodes.core.base.service.IUserService;
import org.nodes.core.base.vo.UserVO;
import org.nodes.core.base.wrapper.UserWrapper;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.utils.Func;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springblade.core.cache.constant.CacheConstant.USER_CACHE;

/**
 * 控制器
 *
 * @author Nodes
 */
@ApiIgnore
@RestController
@RequestMapping(AppConstant.APPLICATION_USER_NAME)
@AllArgsConstructor
public class UserController {

	private IUserService userService;

	/**
	 * 查询单条
	 */
	@ApiLog("用户-查看详情")
	@ApiOperation(value = "查看详情", notes = "传入id")
	@GetMapping("/detail")
	public R<UserVO> detail(User user) {
		User detail = userService.getOne(Condition.getQueryWrapper(user));
		return R.data(UserWrapper.build().entityVO(detail));
	}

	/**
	 * 查询单条
	 */
	@ApiLog("用户-查看详情")
	@ApiOperation(value = "查看详情", notes = "传入id")
	@GetMapping("/info")
	public R<UserVO> info(BladeUser user) {
		User detail = userService.getById(user.getUserId());
		return R.data(UserWrapper.build().entityVO(detail));
	}

	/**
	 * 用户列表
	 */
	@GetMapping("/list")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "account", value = "账号名", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "realName", value = "姓名", paramType = "query", dataType = "string")
	})
	@ApiOperation(value = "列表", notes = "传入account和realName")
	public R<List<UserVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {

		List<User> userList = userService.list(Condition.getQueryWrapper(params, User.class).lambda()
			.func(sql -> {
				if (!AuthUtil.isAdministrator()) {
					sql.notIn(User::getAccount, "admin", "developer");
				}
			}));
		return R.data(UserWrapper.build().listVO(userList));
	}

	/**
	 * 用户分页
	 */
	@GetMapping("/page")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "account", value = "账号名", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "realName", value = "姓名", paramType = "query", dataType = "string")
	})
	@ApiOperation(value = "列表", notes = "传入account和realName")
	public R<IPage<UserVO>> page(@ApiIgnore @RequestParam Map<String, Object> user, Query query, BladeUser bladeUser) {
		QueryWrapper<User> queryWrapper = Condition.getQueryWrapper(user, User.class)
			.notIn("account", "admin", "developer");
		IPage<User> pages = userService.page(Condition.getPage(query),
			(!bladeUser.getTenantId().equals(BladeConstant.ADMIN_TENANT_ID)) ? queryWrapper.lambda()
				.eq(User::getTenantId, bladeUser.getTenantId()) : queryWrapper);
		return R.data(UserWrapper.build().pageVO(pages));
	}

	/**
	 * 新增或修改
	 */
	@ApiLog("用户-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入User")
	@CacheEvict(cacheNames = {USER_CACHE}, allEntries = true)
	public R submit(@Valid @RequestBody User user) {
		if (Func.isEmpty(user.getId())) {
			return R.status(userService.submit(user));
		} else {
			return R.status(userService.updateUser(user));
		}
	}

	/**
	 * 修改
	 */
	@ApiLog("用户-修改")
	@PostMapping("/update")
	@ApiOperation(value = "修改", notes = "传入User")
	@CacheEvict(cacheNames = {USER_CACHE}, allEntries = true)
	public R update(@Valid @RequestBody User user) {
		return R.status(userService.updateUser(user));
	}

	/**
	 * 删除
	 */
	@ApiLog("用户-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入id集合")
	@CacheEvict(cacheNames = {USER_CACHE}, allEntries = true)
	public R remove(@RequestParam String ids) {
		return R.status(userService.removeUser(ids));
	}

	/**
	 * 设置菜单权限
	 *
	 * @param userIds
	 * @param roleIds
	 * @return
	 */
	@ApiLog("用户-权限设置")
	@PostMapping("/grant")
	@ApiOperation(value = "权限设置", notes = "传入roleId集合以及menuId集合")
	public R grant(@ApiParam(value = "userId集合", required = true) @RequestParam String userIds,
				   @ApiParam(value = "roleId集合", required = true) @RequestParam String roleIds) {
		boolean temp = userService.grant(userIds, roleIds);
		return R.status(temp);
	}

	/**
	 * 重置密码
	 *
	 * @param userIds
	 * @return
	 */
	@ApiLog("用户-重置密码")
	@PostMapping("/reset-password")
	@ApiOperation(value = "初始化密码", notes = "传入userId集合")
	@CacheEvict(cacheNames = {USER_CACHE}, allEntries = true)
	public R resetPassword(@ApiParam(value = "userId集合", required = true) @RequestParam String userIds) {
		boolean temp = userService.resetPassword(userIds);
		return R.status(temp);
	}

	/**
	 * 修改密码
	 *
	 * @param oldPassword
	 * @param newPassword
	 * @param newPassword1
	 * @return
	 */
	@ApiLog("用户-修改密码")
	@PostMapping("/update-password")
	@ApiOperation(value = "修改密码", notes = "传入密码")
	@CacheEvict(cacheNames = {USER_CACHE}, allEntries = true)
	public R updatePassword(BladeUser user, @ApiParam(value = "旧密码", required = true) @RequestParam String oldPassword,
							@ApiParam(value = "新密码", required = true) @RequestParam String newPassword,
							@ApiParam(value = "新密码", required = true) @RequestParam String newPassword1) {
		boolean temp = userService.updatePassword(user.getUserId(), oldPassword, newPassword, newPassword1);
		return R.status(temp);
	}

	/**
	 * 修改个人信息
	 *
	 * @param user
	 * @return
	 */
	@ApiLog("用户-修改个人信息")
	@PostMapping("/update-info")
	@ApiOperation(value = "修改个人信息", notes = "传入User")
	@CacheEvict(cacheNames = {USER_CACHE}, allEntries = true)
	public R updateInfo(BladeUser bladeUser, @Valid @RequestBody User user) {
		return R.status(userService.updateUserInfo(bladeUser, user));
	}
}
