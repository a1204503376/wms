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
package org.nodes.core.base.wrapper;

import io.jsonwebtoken.lang.Collections;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.SysCache;
import org.nodes.core.base.entity.*;
import org.nodes.core.base.service.IDeptService;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.base.service.IRoleService;
import org.nodes.core.base.service.ITenantService;
import org.nodes.core.tool.utils.NodesUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.nodes.core.base.vo.UserVO;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author Nodes
 */
public class UserWrapper extends BaseEntityWrapper<User, UserVO> {
	List<Dept> depts = new ArrayList<>();
	List<Role> roles = new ArrayList<>();
	List<Tenant> tenants = new ArrayList<>();
	{
		IRoleService roleService = SpringUtil.getBean(IRoleService.class);
		IDeptService deptService = SpringUtil.getBean(IDeptService.class);
		ITenantService tenantService = SpringUtil.getBean(ITenantService.class);
		depts = deptService.list();
		roles = roleService.list();
		tenants = tenantService.list();
	}
	public static UserWrapper build() {
		return new UserWrapper();
	}

	@Override
	public UserVO entityVO(User user) {
		UserVO userVO = Objects.requireNonNull(BeanUtil.copy(user, UserVO.class));
		List<Dept> deptList = depts.stream().filter(dept -> dept.getId().equals(Func.toLong(user.getDeptId()))).collect(Collectors.toList());
		List<Role> roleList = roles.stream().filter(role -> role.getId().equals(Func.toLong(user.getRoleId()))).collect(Collectors.toList());
		List<Tenant> tenantList = tenants.stream().filter(tenant -> tenant.getId().equals(Func.toLong(user.getTenantId()))).collect(Collectors.toList());
		List<String> roleName = NodesUtil.toList(roleList,Role::getRoleName);
		List<String> deptName = NodesUtil.toList(deptList,Dept::getDeptName);
		List<String> tenantName=NodesUtil.toList(tenantList,Tenant::getTenantName);
		userVO.setRoleName(Func.join(roleName));
		userVO.setDeptName(Func.join(deptName));
		userVO.setTenantName(Func.join(tenantName));
		userVO.setSexName(DictCache.getValue("sex", Func.toInt(user.getSex())));
		return userVO;
	}

}
