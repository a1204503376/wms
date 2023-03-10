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
package org.nodes.core.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.nodes.core.base.vo.PdaMenuVO;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.support.Kv;
import org.nodes.core.base.entity.Menu;
import org.nodes.core.base.vo.MenuVO;

import java.util.List;

/**
 * 服务类
 *
 * @author Nodes
 */
public interface IMenuService extends IService<Menu> {

	/**
	 * 菜单树形结构
	 *
	 * @param roleId
	 * @param topMenuId
	 * @return
	 */
	List<PdaMenuVO> routesPDA(String roleId, Long topMenuId);

	/**
	 * 菜单树形结构
	 *
	 * @param roleId
	 * @param topMenuId
	 * @return
	 */
	List<MenuVO> routes(String roleId, Long topMenuId, Integer systemType);

	/**
	 * 菜单树形结构
	 *
	 * @param roleId
	 * @param topMenuId
	 * @return
	 */
	List<MenuVO> routesExt(String roleId, Long topMenuId, Integer systemType);

	/**
	 * 按钮树形结构
	 *
	 * @param roleId
	 * @return
	 */
	List<MenuVO> buttons(String roleId);

	/**
	 * 树形结构
	 *
	 * @return
	 */
	List<MenuVO> tree();

	/**
	 * 授权树形结构
	 *
	 * @param user
	 * @return
	 */
	List<MenuVO> grantTree(BladeUser user);

	/**
	 * 顶部菜单树形结构
	 *
	 * @param user
	 * @return
	 */
	List<MenuVO> grantTopTree(BladeUser user);

	/**
	 * 数据权限授权树形结构
	 *
	 * @param user
	 * @return
	 */
	List<MenuVO> grantDataScopeTree(BladeUser user);

	/**
	 * 接口权限授权树形结构
	 *
	 * @param user
	 * @return
	 */
	List<MenuVO> grantApiScopeTree(BladeUser user);

	/**
	 * 默认选中节点
	 *
	 * @param roleIds
	 * @return
	 */
	List<String> roleTreeKeys(String roleIds);

	/**
	 * 默认选中节点
	 *
	 * @param topMenuIds
	 * @return
	 */
	List<String> topTreeKeys(String topMenuIds);

	/**
	 * 默认选中节点
	 *
	 * @param roleIds
	 * @return
	 */
	List<String> dataScopeTreeKeys(String roleIds);

	/**
	 * 默认选中节点
	 *
	 * @param roleIds
	 * @return
	 */
	List<String> apiScopeTreeKeys(String roleIds);

	/**
	 * 获取配置的角色权限
	 *
	 * @param user
	 * @return
	 */
	List<Kv> authRoutes(BladeUser user);

	/**
	 * 删除菜单
	 *
	 * @param ids
	 * @return
	 */
	boolean removeMenu(String ids);

	/**
	 * 控制菜单显隐
	 * @param id
	 * @param type
	 * @return
	 */
	boolean changeVisible(Long id, Integer type);

	/**
	 * 提交
	 *
	 * @param menu
	 * @return
	 */
	boolean submit(Menu menu);

}
