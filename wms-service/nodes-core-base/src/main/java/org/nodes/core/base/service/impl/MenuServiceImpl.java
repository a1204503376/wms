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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nodes.core.base.cache.*;
import org.nodes.core.base.mapper.UserMapper;
import org.nodes.core.constant.CommonConstant;
import org.nodes.core.tool.utils.NodesUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.constant.RoleConstant;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.support.Kv;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.nodes.core.base.dto.MenuDTO;
import org.nodes.core.base.entity.*;
import org.nodes.core.base.enums.MenuCategoryEnum;
import org.nodes.core.base.mapper.MenuMapper;
import org.nodes.core.base.service.*;
import org.nodes.core.base.vo.MenuVO;
import org.nodes.core.base.wrapper.MenuWrapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springblade.core.cache.constant.CacheConstant.MENU_CACHE;

/**
 * 服务实现类
 *
 * @author Nodes
 */
@Slf4j
@Service
@AllArgsConstructor
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class MenuServiceImpl <M extends MenuMapper, T extends Menu> extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

	IRoleService roleService;
	IRoleMenuService roleMenuService;
	IRoleScopeService roleScopeService;
	ITopMenuSettingService topMenuSettingService;

	@Override
	public List<MenuVO> routes(String roleId, Long topMenuId, Integer systemType) {
		if (StringUtil.isBlank(roleId)) {
			return null;
		}
		/*List<Menu> allMenus = MenuCache.list().stream().filter(u -> {
			boolean result = Func.isNotEmpty(u.getCategory()) && u.getCategory().equals(1)
				&& Func.isNotEmpty(u.getSystemType()) && u.getSystemType().equals(systemType);
			if (!SecureUtil.isDeveloper()) {
				return result && u.getIsVisible().equals(0);
			}
			return result;
		}).collect(Collectors.toList());*/

		List<Menu> allMenus = super.list(Condition.getQueryWrapper(new Menu())
		.lambda()
		.isNotNull(Menu::getCategory)
		.eq(Menu::getCategory,1)
		.isNotNull(Menu::getSystemType)
		.eq(Menu::getSystemType,systemType)
		.func(i -> {
			if(!SecureUtil.isDeveloper()){
				i.eq(Menu::getIsVisible,0);
			}
		})
		);

		List<Menu> roleMenus = null;
		if (SecureUtil.isAdministrator() && Func.isEmpty(topMenuId)) {
			roleMenus = allMenus;
		} else if (Func.isNotEmpty(topMenuId)) {
			List<TopMenuSetting> topMenuSettingList = topMenuSettingService.list(
				Condition.getQueryWrapper(new TopMenuSetting())
				.lambda()
				.eq(TopMenuSetting::getTopMenuId, topMenuId));
			roleMenus = allMenus.stream().filter(u -> {
				return topMenuSettingList.stream().filter(item->Func.equals(item.getMenuId(), u.getId()))
					.count() > 0L;
			}).collect(Collectors.toList());
		} else {
			roleMenus = baseMapper.roleMenu(Func.toLongList(roleId), topMenuId).stream().filter(u -> {
				return u.getSystemType().equals(systemType);
			}).collect(Collectors.toList());
		}
		return buildRoutes(allMenus, roleMenus);
	}

	@Override
	public List<MenuVO> routesPDA(String roleId, Long topMenuId) {
		if (StringUtil.isBlank(roleId)) {
			return null;
		}
		/*List<Menu> allMenus = MenuCache.list().stream().filter(u -> {
			boolean result = Func.isNotEmpty(u.getCategory()) && u.getCategory().equals(1);
			if (!SecureUtil.isDeveloper()) {
				return result && Func.isNotEmpty(u.getIsVisible()) && u.getIsVisible().equals(0);
			}
			return result;
		}).collect(Collectors.toList());*/
		List<Menu> allMenus = super.list(Condition.getQueryWrapper(new Menu())
		.lambda()
		.isNotNull(Menu::getCategory)
		.eq(Menu::getCategory,1)
		.func(i -> {
			if (!SecureUtil.isDeveloper()){
				i.isNotNull(Menu::getIsVisible);
				i.eq(Menu::getIsVisible,0);
			}
		}));

		List<Menu> roleMenus = null;
		if (SecureUtil.isAdministrator() && Func.isEmpty(topMenuId)) {
			roleMenus = allMenus;
		} else {
			roleMenus = baseMapper.roleMenu(Func.toLongList(roleId), topMenuId).stream().filter(u -> {
				return u.getSystemType().equals(0);
			}).collect(Collectors.toList());
		}
		return buildRoutes(allMenus, roleMenus);
	}

	@Override
	public List<MenuVO> routesExt(String roleId, Long topMenuId, Integer systemType) {
		if (StringUtil.isBlank(roleId)) {
			return null;
		}
		/*List<Menu> allMenus = MenuCache.list().stream().filter(u -> {
			return Func.isNotEmpty(u.getCategory()) && u.getCategory().equals(1);
		}).collect(Collectors.toList());*/
		List<Menu> allMenus = super.list(Condition.getQueryWrapper(new Menu())
		.lambda()
		.isNotNull(Menu::getCategory)
		.eq(Menu::getCategory,1)
		);
		List<Menu> roleMenus = baseMapper.roleMenuExt(Func.toLongList(roleId), topMenuId);
		return buildRoutes(allMenus, roleMenus);
	}

	private List<MenuVO> buildRoutes(List<Menu> allMenus, List<Menu> roleMenus) {
		List<Menu> routes = new LinkedList<>(roleMenus);
		roleMenus.forEach(roleMenu -> recursion(allMenus, routes, roleMenu));
		routes.sort(Comparator.comparing(Menu::getSort));
		MenuWrapper menuWrapper = new MenuWrapper();
		List<Menu> collect = routes.stream().filter(x -> Func.equals(x.getCategory(), 1)).collect(Collectors.toList());
		return menuWrapper.listNodeVO(collect);
	}

	private void recursion(List<Menu> allMenus, List<Menu> routes, Menu roleMenu) {
		Optional<Menu> menu = allMenus.stream().filter(x -> Func.equals(x.getId(), roleMenu.getParentId())).findFirst();
		if (menu.isPresent() && !routes.contains(menu.get())) {
			routes.add(menu.get());
			recursion(allMenus, routes, menu.get());
		}
	}

	@Override
	public List<MenuVO> buttons(String roleId) {
		// 获取所有菜单
		List<Menu> allMenu = MenuCache.list();
		// 获取所有按钮信息
		List<Menu> buttonList = allMenu.stream().filter(u -> {
			return MenuCategoryEnum.BUTTON.getIndex().equals(u.getCategory());
		}).collect(Collectors.toList());
		// 获取所有按钮的父级
		List<Menu> buttonParentList = allMenu.stream().filter(u -> {
			return MenuCategoryEnum.MENU.getIndex().equals(u.getCategory())
				&& buttonList.stream().filter(button -> u.getId().equals(button.getParentId())).count() > 0;
		}).collect(Collectors.toList());

		if (!SecureUtil.isAdministrator()) {
			// 不是 管理员/开发者 角色的情况下，需要对菜单做权限筛选
			// 1. 获取菜单权限信息
			List<RoleMenu> roleMenuList = roleMenuService.list(Condition.getQueryWrapper(new RoleMenu())
				.lambda()
				.in(RoleMenu::getRoleId, Func.toLongList(roleId)));
			// 2. 筛选菜单按钮(删除没有权限的菜单按钮)
			buttonList.removeIf(new Predicate<Menu>() {
				@Override
				public boolean test(Menu menu) {
					return roleMenuList.stream().filter(u -> {
						return u.getMenuId().equals(menu.getId());
					}).findFirst().orElse(null) == null;
				}
			});
		}
		if (!SecureUtil.isDeveloper()) {
			buttonList.removeIf(new Predicate<Menu>() {
				@Override
				public boolean test(Menu menu) {
					return menu.getIsVisible() != 0;
				}
			});
		}
		// 服务器端返回的数据
		List<Menu> menuList = new ArrayList() {{
			addAll(buttonList);
			addAll(buttonParentList);
		}};
		return MenuWrapper.build().listNodeVO(menuList);
	}

	@Override
	public List<MenuVO> tree() {
		return ForestNodeMerger.merge(baseMapper.tree());
	}

	@Override
	public List<MenuVO> grantTree(BladeUser user) {
		List<MenuVO> menuList = null;
		Role role = (Role) roleService.getById(user.getRoleId());
		if (role.getRoleAlias().equals(RoleConstant.DEVELOPER)) {
			menuList = baseMapper.grantTree(null);
		} else if (role.getRoleAlias().equals(RoleConstant.ADMINISTRATOR)) {
			menuList = baseMapper.grantTree(0);
		} else {
			menuList = baseMapper.grantTreeByRole(Func.toLongList(user.getRoleId()));
		}
		return ForestNodeMerger.merge(menuList);
	}

	@Override
	public List<MenuVO> grantTopTree(BladeUser user) {
		return ForestNodeMerger.merge(user.getTenantId().equals(BladeConstant.ADMIN_TENANT_ID) ? baseMapper.grantTopTree() : baseMapper.grantTopTreeByRole(Func.toLongList(user.getRoleId())));
	}

	@Override
	public List<MenuVO> grantDataScopeTree(BladeUser user) {
		return ForestNodeMerger.merge(user.getTenantId().equals(BladeConstant.ADMIN_TENANT_ID) ? baseMapper.grantDataScopeTree() : baseMapper.grantDataScopeTreeByRole(Func.toLongList(user.getRoleId())));
	}

	@Override
	public List<MenuVO> grantApiScopeTree(BladeUser user) {
		return ForestNodeMerger.merge(user.getTenantId().equals(BladeConstant.ADMIN_TENANT_ID) ? baseMapper.grantApiScopeTree() : baseMapper.grantApiScopeTreeByRole(Func.toLongList(user.getRoleId())));
	}

	@Override
	public List<String> roleTreeKeys(String roleIds) {
		List<RoleMenu> roleMenus = roleMenuService.list(Wrappers.<RoleMenu>query().lambda().in(RoleMenu::getRoleId, Func.toLongList(roleIds)));
		return roleMenus.stream().map(roleMenu -> Func.toStr(roleMenu.getMenuId())).collect(Collectors.toList());
	}

	@Override
	public List<String> topTreeKeys(String topMenuIds) {
		List<TopMenuSetting> settings = topMenuSettingService.list(Wrappers.<TopMenuSetting>query().lambda().in(TopMenuSetting::getTopMenuId, Func.toLongList(topMenuIds)));
		return settings.stream().map(setting -> Func.toStr(setting.getMenuId())).collect(Collectors.toList());
	}

	@Override
	public List<String> dataScopeTreeKeys(String roleIds) {
		List<RoleScope> roleScopes = roleScopeService.list(Wrappers.<RoleScope>query().lambda().in(RoleScope::getRoleId, Func.toLongList(roleIds)));
		return roleScopes.stream().map(roleScope -> Func.toStr(roleScope.getScopeId())).collect(Collectors.toList());
	}

	@Override
	public List<String> apiScopeTreeKeys(String roleIds) {
		List<RoleScope> roleScopes = roleScopeService.list(Wrappers.<RoleScope>query().lambda().in(RoleScope::getRoleId, Func.toLongList(roleIds)));
		return roleScopes.stream().map(roleScope -> Func.toStr(roleScope.getScopeId())).collect(Collectors.toList());
	}

	@Override
	@Cacheable(cacheNames = MENU_CACHE, key = "'auth:routes:' + #user.roleId")
	public List<Kv> authRoutes(BladeUser user) {
		List<MenuDTO> routes = baseMapper.authRoutes(Func.toLongList(user.getRoleId()));
		List<Kv> list = new ArrayList<>();
		routes.forEach(route ->
			list.add(
				Kv.create().set(route.getPath(),
					Kv.create().set("authority", Func.toStrArray(route.getAlias())))));
		return list;
	}

	@Override
	public boolean removeMenu(String ids) {
		Integer cnt = baseMapper.selectCount(Wrappers.<Menu>query().lambda().in(Menu::getParentId, Func.toLongList(ids)));
		if (cnt > 0) {
			throw new ServiceException("请先删除子节点!");
		}
		List<Long> idList = Func.toLongList(ids);
		boolean result = removeByIds(idList);
		//MenuCache.remove(idList);
		return result;
	}

	@Override
	public boolean changeVisible(Long id, Integer type) {
		if (!SecureUtil.isDeveloper()) {
			throw new ServiceException("您没有权限操作！");
		}
		// 获取所有菜单
		List<Menu> menuAllList = super.list();
		// 获取该菜单下所有子级
		List<Menu> menuList = new ArrayList<>();
		menuList.add(super.getById(id));
		List<Long> idList = new ArrayList<>();
		idList.add(id);
		do {
			List<Long> finalIdList = idList;
			List<Menu> childrenList = menuAllList.stream().filter(u -> {
				return finalIdList.contains(u.getParentId());
			}).collect(Collectors.toList());
			if (Func.isEmpty(childrenList)) {
				// 没有子级，结束循环
				break;
			}
			// 存在子级，继续循环获取
			menuList.addAll(childrenList);
			idList = NodesUtil.toList(childrenList, Menu::getId);
		} while (true);

		boolean result = super.update(Wrappers.<Menu>update().lambda()
			.set(Menu::getIsVisible, type)
			.in(Menu::getId, NodesUtil.toList(menuList, Menu::getId)));

		for (Menu menu : menuList) {
			menu.setIsVisible(type);
			//MenuCache.saveOrUpdate(menu);
		}

		return result;
	}

	@Override
	public boolean submit(Menu menu) {
		BladeUser user = AuthUtil.getUser();
		if (Func.isNotEmpty(menu.getId())) {
			menu.setCreateUser(user.getUserId());
		}
		menu.setUpdateUser(user.getUserId());
		menu.setIsDeleted(BladeConstant.DB_NOT_DELETED);
		menu.setIsVisible(0);
		if (Func.isEmpty(menu.getParentId())) {
			menu.setParentId(CommonConstant.TOP_PARENT_ID);
		}
		boolean result = saveOrUpdate(menu);
		if (result) {
			menu = super.getById(menu.getId());
			//MenuCache.saveOrUpdate(menu);
		}
		return result;
	}

}
