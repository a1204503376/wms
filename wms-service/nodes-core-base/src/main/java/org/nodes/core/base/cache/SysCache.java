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
import org.springblade.core.tool.utils.SpringUtil;
import org.nodes.core.base.entity.*;
import org.nodes.core.base.service.*;

import java.util.List;

import static org.springblade.core.cache.constant.CacheConstant.SYS_CACHE;

/**
 * 系统缓存
 *
 * @author Nodes
 */
public class SysCache {


	private static final String DEPT_ID = "dept:id:";
	private static final String DEPT_NAME_ID = "deptName:id:";
	private static final String DEPT_NAMES_ID = "deptNames:id:";
	private static final String ROLE_ID = "role:id:";
	private static final String ROLE_NAME_ID = "roleName:id:";
	private static final String ROLE_NAMES_ID = "roleNames:id:";
	private static final String ROLE_ALIAS_ID = "roleAlias:id:";
	private static final String ROLE_ALIASES_ID = "roleAliases:id:";
	private static final String TENANT_ID = "tenant:id:";
	private static final String TENANT_Names_ID = "tenantNames:id:";

	private static IDeptService deptService;
	private static IRoleService roleService;
	private static ITenantService tenantService;

	static {
		deptService = SpringUtil.getBean(IDeptService.class);
		roleService = SpringUtil.getBean(IRoleService.class);
		tenantService = SpringUtil.getBean(ITenantService.class);
	}
	/**
	 * 获取机构
	 *
	 * @param id 主键
	 * @return 机构
	 */
	public static Dept getDept(Long id) {
		return CacheUtil.get(SYS_CACHE, DEPT_ID, id, () -> deptService.getById(id));
	}

	/**
	 * 获取机构名
	 *
	 * @param id 主键
	 * @return 机构名
	 */
	public static String getDeptName(Long id) {
		return CacheUtil.get(SYS_CACHE, DEPT_NAME_ID, id, () -> deptService.getById(id).getDeptName());
	}

	/**
	 * 获取角色
	 *
	 * @param id 主键
	 * @return Role
	 */
	public static Role getRole(Long id) {
		return CacheUtil.get(SYS_CACHE, ROLE_ID, id, () -> (Role)roleService.getById(id));
	}

	/**
	 * 获取机构名集合
	 *
	 * @param deptIds 主键集合
	 * @return 机构名
	 */
	public static List<String> getDeptNames(String deptIds) {
		return CacheUtil.get(SYS_CACHE, DEPT_NAMES_ID, deptIds, () -> deptService.getDeptNames(deptIds));
	}

	/**
	 * 获取角色名集合
	 *
	 * @param roleIds 主键集合
	 * @return 角色名
	 */
	public static List<String> getRoleNames(String roleIds) {
		return CacheUtil.get(SYS_CACHE, ROLE_NAMES_ID, roleIds, () -> roleService.getRoleNames(roleIds));
	}

	/**
	 * 获取角色别名集合
	 *
	 * @param roleIds 主键集合
	 * @return 角色别名
	 */
	public static List<String> getRoleAliases(String roleIds) {
		return CacheUtil.get(SYS_CACHE, ROLE_ALIASES_ID, roleIds, () -> roleService.getRoleAliases(roleIds));
	}

	/**
	 * 获取租户
	 *
	 * @param id 主键
	 * @return Tenant
	 */
	public static Tenant getTenant(Long id) {
		return CacheUtil.get(SYS_CACHE, TENANT_ID, id, () -> tenantService.getById(id));
	}

	/**
	 * 获取租户名集合
	 *
	 * @param tenantIds 主键集合
	 * @return 机构名
	 */
	public static List<String> getTenantNames(String tenantIds) {
		return CacheUtil.get(SYS_CACHE, TENANT_Names_ID, tenantIds, () -> tenantService.getTenantNames(tenantIds));
	}
}
