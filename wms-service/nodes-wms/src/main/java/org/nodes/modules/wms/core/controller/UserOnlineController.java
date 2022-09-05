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
import lombok.AllArgsConstructor;
import org.nodes.wms.core.system.entity.UserOnline;
import org.nodes.wms.core.system.service.IUserOnlineService;
import org.nodes.wms.core.system.vo.UserOnlineVO;
import org.nodes.wms.core.system.wrapper.UserOnlineWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 在线用户 控制器
 *
 * @author pengwei
 * @since 2020-04-01
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/core/userOnline")
@Api(value = "在线用户", tags = "在线用户接口")
public class UserOnlineController extends BladeController {

	private IUserOnlineService userOnlineService;


	/**
	 * 获取在线用户列表
	 */
	@GetMapping("/list")
	@ApiOperation(value = "列表", notes = "传入userOnline")
	public R<List<UserOnlineVO>> list(UserOnline userOnline) {
		List<UserOnline> list = userOnlineService.list(Condition.getQueryWrapper(userOnline));
		return R.data(UserOnlineWrapper.build().listVO(list));
	}
}
