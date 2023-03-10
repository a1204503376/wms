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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.base.entity.Role;
import org.nodes.core.base.entity.RoleMenu;
import org.nodes.core.base.entity.RoleScope;
import org.nodes.core.base.entity.User;
import org.nodes.core.base.mapper.RoleMapper;
import org.nodes.core.base.service.IRoleMenuService;
import org.nodes.core.base.service.IRoleScopeService;
import org.nodes.core.base.service.IRoleService;
import org.nodes.core.base.service.IUserService;
import org.nodes.core.base.vo.RoleVO;
import org.nodes.core.constant.WmsAppConstant;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.constant.RoleConstant;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.utils.CollectionUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ???????????????
 *
 * @author Nodes
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class RoleServiceImpl<M extends RoleMapper, T extends Role> extends BaseServiceImpl<RoleMapper, Role>
	implements IRoleService<T> {

	@Autowired
	IRoleMenuService roleMenuService;
	@Autowired
	IRoleScopeService roleScopeService;

	@Override
	public List<RoleVO> tree(String tenantId) {
		String userRole = AuthUtil.getUserRole();
		String excludeRole = null;
		if (!CollectionUtil.contains(Func.toStrArray(userRole), RoleConstant.DEVELOPER) &&
			!CollectionUtil.contains(Func.toStrArray(userRole), RoleConstant.ADMINISTRATOR)) {
			excludeRole = RoleConstant.ADMIN;
		}
		return ForestNodeMerger.merge(baseMapper.tree(tenantId, excludeRole));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean grant(@NotEmpty List<Long> roleIds, List<Long> menuIds, List<Long> dataScopeIds, List<Long> apiScopeIds) {
		return grantRoleMenu(roleIds, menuIds) && grantDataScope(roleIds, dataScopeIds) && grantApiScope(roleIds, apiScopeIds);
	}

	private boolean grantRoleMenu(List<Long> roleIds, List<Long> menuIds) {
		// ?????????????????????????????????
//		roleMenuService.remove(Wrappers.<RoleMenu>update().lambda().in(RoleMenu::getRoleId, roleIds));
		roleMenuService.removeByRoleIds(roleIds);
		// ????????????
		List<RoleMenu> roleMenus = new ArrayList<>();
		roleIds.forEach(roleId -> menuIds.forEach(menuId -> {
			RoleMenu roleMenu = new RoleMenu();
			roleMenu.setRoleId(roleId);
			roleMenu.setMenuId(menuId);
			roleMenus.add(roleMenu);
		}));
		// ????????????
		roleMenuService.saveBatch(roleMenus);
		// ????????????????????????????????????
		recursionRoleMenu(roleIds, menuIds);
		return true;
	}

	private void recursionRoleMenu(List<Long> roleIds, List<Long> menuIds) {
		roleIds.forEach(roleId -> baseMapper.selectList(Wrappers.<Role>query().lambda().eq(Role::getParentId, roleId)).forEach(role -> {
			List<RoleMenu> roleMenuList = roleMenuService.list(Wrappers.<RoleMenu>query().lambda().eq(RoleMenu::getRoleId, role.getId()));
			// ????????????????????????????????????????????????
			List<Long> collectRoleMenuIds = roleMenuList.stream().map(RoleMenu::getMenuId).filter(menuId -> !menuIds.contains(menuId)).collect(Collectors.toList());
			if (collectRoleMenuIds.size() > 0) {
				// ???????????????????????????????????????
				roleMenuService.remove(Wrappers.<RoleMenu>update().lambda().eq(RoleMenu::getRoleId, role.getId()).in(RoleMenu::getMenuId, collectRoleMenuIds));
				// ????????????????????????????????????
				recursionRoleMenu(Collections.singletonList(role.getId()), menuIds);
			}
		}));
	}

	private boolean grantDataScope(List<Long> roleIds, List<Long> dataScopeIds) {
		// ???????????????????????????????????????
		roleScopeService.remove(Wrappers.<RoleScope>update().lambda()
			.eq(RoleScope::getScopeCategory, WmsAppConstant.DATA_SCOPE_CATEGORY)
			.in(RoleScope::getRoleId, roleIds));
		// ????????????
		List<RoleScope> roleDataScopes = new ArrayList<>();
		roleIds.forEach(roleId -> dataScopeIds.forEach(scopeId -> {
			RoleScope roleScope = new RoleScope();
			roleScope.setScopeCategory(WmsAppConstant.DATA_SCOPE_CATEGORY);
			roleScope.setRoleId(roleId);
			roleScope.setScopeId(scopeId);
			roleDataScopes.add(roleScope);
		}));
		// ????????????
		roleScopeService.saveBatch(roleDataScopes);
		return true;
	}

	private boolean grantApiScope(List<Long> roleIds, List<Long> apiScopeIds) {
		// ???????????????????????????????????????
		roleScopeService.remove(Wrappers.<RoleScope>update().lambda()
			.eq(RoleScope::getScopeCategory, WmsAppConstant.API_SCOPE_CATEGORY)
			.in(RoleScope::getRoleId, roleIds));
		// ????????????
		List<RoleScope> roleApiScopes = new ArrayList<>();
		roleIds.forEach(roleId -> apiScopeIds.forEach(scopeId -> {
			RoleScope roleScope = new RoleScope();
			roleScope.setScopeCategory(WmsAppConstant.API_SCOPE_CATEGORY);
			roleScope.setScopeId(scopeId);
			roleScope.setRoleId(roleId);
			roleApiScopes.add(roleScope);
		}));
		// ????????????
		roleScopeService.saveBatch(roleApiScopes);
		return true;
	}

	@Override
	public String getRoleIds(String tenantId, String roleNames) {
		List<Role> roleList = baseMapper.selectList(Wrappers.<Role>query().lambda().eq(Role::getTenantId, tenantId).in(Role::getRoleName, Func.toStrList(roleNames)));
		if (roleList != null && roleList.size() > 0) {
			return roleList.stream().map(role -> Func.toStr(role.getId())).distinct().collect(Collectors.joining(","));
		}
		return null;
	}

	@Override
	public List<String> getRoleNames(String roleIds) {
		return baseMapper.getRoleNames(Func.toLongArray(roleIds));
	}

	@Override
	public List<String> getRoleAliases(String roleIds) {
		return baseMapper.getRoleAliases(Func.toLongArray(roleIds));
	}

	@Override
	public boolean submit(RoleVO role) {
		if (!SecureUtil.isAdministrator()) {
			if (Func.toStr(role.getRoleAlias()).equals(RoleConstant.ADMINISTRATOR)) {
				throw new ServiceException("??????????????????????????????");
			}
		}
		if (Func.isEmpty(role.getParentId())) {
			role.setParentId(BladeConstant.TOP_PARENT_ID);
		}
		return super.saveOrUpdate(role);
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = super.removeByIds(idList);
		IUserService userService = SpringUtil.getBean(IUserService.class);
		long cnt = userService.count(Condition.getQueryWrapper(new User())
		.lambda()
		.in(User::getRoleId,idList));
		if (cnt > 0L) {
			throw new ServiceException("???????????????????????????");
		}
		return result;
	}

	@Override
	public boolean removeById(Serializable id) {
		List<Long> idList = new ArrayList() {{
			add(id);
		}};
		return this.removeByIds(idList);
	}
}
