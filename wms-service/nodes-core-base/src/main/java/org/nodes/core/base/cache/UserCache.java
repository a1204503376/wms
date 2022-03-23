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
package org.nodes.core.base.cache;

import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.nodes.core.base.entity.User;
import org.nodes.core.base.service.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springblade.core.cache.constant.CacheConstant.USER_CACHE;

/**
 * 用户信息缓存
 *
 * @author Nodes
 */
public class UserCache {

	private static IUserService userService;

	static final String USER_ID = "user:id:";

	static {
		userService = SpringUtil.getBean(IUserService.class);
	}

	/**
	 * 获取用户信息
	 *
	 * @param id 用户id
	 * @return
	 */
	public static User getById(Long id) {
		return CacheUtil.get(USER_CACHE, USER_ID, id, () -> userService.getById(id));
	}
}
