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
package org.nodes.core.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.nodes.core.base.entity.*;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.base.mapper.UserMapper;
import org.nodes.core.base.service.*;
import org.nodes.core.base.cache.*;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tenant.TenantId;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.jackson.JsonUtil;
import org.springblade.core.tool.support.Kv;
import org.springblade.core.tool.utils.DesUtil;
import org.springblade.core.tool.utils.Func;
import org.nodes.core.base.mapper.TenantMapper;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.nodes.core.constant.TenantConstant.DES_KEY;

/**
 * 服务实现类
 *
 * @author Nodes
 */
@Service
@AllArgsConstructor
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class TenantServiceImpl <M extends TenantMapper, T extends Tenant> extends BaseServiceImpl<TenantMapper, Tenant> implements ITenantService {

	private TenantId tenantId;
	private final IRoleService roleService;
	private final IDeptService deptService;
	private IUserService userService;

	@Override
	public IPage<Tenant> selectTenantPage(IPage<Tenant> page, Tenant tenant) {
		return page.setRecords(baseMapper.selectTenantPage(page, tenant));
	}

	@Override
	public Tenant getByTenantId(String tenantId) {
		return getOne(Wrappers.<Tenant>query().lambda().eq(Tenant::getTenantId, tenantId));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean submitTenant(Tenant tenant) {
		if (Func.isEmpty(tenant.getId())) {
			List<Tenant> tenants = baseMapper.selectList(Wrappers.<Tenant>query().lambda().eq(Tenant::getIsDeleted, BladeConstant.DB_NOT_DELETED));
			List<String> codes = tenants.stream().map(Tenant::getTenantId).collect(Collectors.toList());
			String tenantId = getTenantId(codes);
			tenant.setTenantId(tenantId);
			// 新建租户对应的默认角色
			Role role = new Role();
			role.setTenantId(tenantId);
			role.setParentId(BladeConstant.TOP_PARENT_ID);
			role.setRoleName("管理员");
			role.setRoleAlias("administrator");
			role.setSort(2);
			role.setIsDeleted(0);
			roleService.save(role);
			// 新建租户对应的默认机构
			Dept dept = new Dept();
			dept.setTenantId(tenantId);
			dept.setParentId(BladeConstant.TOP_PARENT_ID);
			dept.setDeptName(tenant.getTenantName());
			dept.setFullName(tenant.getTenantName());
			dept.setSort(2);
			dept.setIsDeleted(0);
			deptService.save(dept);
			// 新建租户对应的默认管理用户
			User user = new User();
			user.setTenantId(tenantId);
			user.setName("admin");
			user.setRealName("admin");
			user.setAccount("admin");
			// 获取参数配置的密码
			String password = ParamCache.getValue(ParamEnum.USER_DEFAULT_PASSWORD.getKey());
			user.setPassword(Func.isNotEmpty(password) ? password : "");
			if (Func.isNotEmpty(user.getPassword())) {
				user.setPassword(user.getPassword());
			}
			user.setRoleId(String.valueOf(role.getId()));
			user.setDeptId(String.valueOf(dept.getId()));
			user.setBirthday(new Date());
			user.setSex(1);
			user.setIsDeleted(BladeConstant.DB_NOT_DELETED);
			userService.submit(user);
		}
		boolean result = super.saveOrUpdate(tenant);
		if (result) {
			//TenantCache.saveOrUpdate(tenant);
		}
		return result;
	}

	@Override
	public List<String> getTenantNames(String tenantIds) {
		return baseMapper.getTenantNames(Func.toLongArray(tenantIds));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeTenant(List<Long> ids) {
		List<Tenant> tenantList = this.list(Wrappers.<Tenant>query().lambda().in(Tenant::getId, ids));
		List<String> tenantIds = tenantList.stream().map(tenant -> Func.toStr(tenant.getTenantId())).distinct().collect(Collectors.toList());
		if (tenantIds.contains(BladeConstant.ADMIN_TENANT_ID)) {
			throw new ServiceException("不可删除管理租户!");
		}
		boolean tenantTemp = this.deleteLogic(ids);
		boolean userTemp = userService.remove(Wrappers.<User>query().lambda().in(User::getTenantId, tenantIds));
		return tenantTemp && userTemp;
	}

	@Override
	public boolean setting(Integer accountNumber, Date expireTime, Integer whMax, String ids) {
		Func.toLongList(ids).forEach(id -> {
			Kv kv = Kv.create().set("accountNumber", accountNumber)
				.set("expireTime", expireTime)
				.set("whMax", whMax)
				.set("id", id);
			String licenseKey = DesUtil.encryptToHex(JsonUtil.toJson(kv), DES_KEY);
			update(
				Wrappers.<Tenant>update().lambda()
					.set(Tenant::getAccountNumber, accountNumber)
					.set(Tenant::getExpireTime, expireTime)
					.set(Tenant::getWhMax, whMax)
					.set(Tenant::getLicenseKey, licenseKey)
					.eq(Tenant::getId, id)
			);
		});
		return true;
	}

	private String getTenantId(List<String> codes) {
		String code = tenantId.generate();
		if (codes.contains(code)) {
			return getTenantId(codes);
		}
		return code;
	}
}
