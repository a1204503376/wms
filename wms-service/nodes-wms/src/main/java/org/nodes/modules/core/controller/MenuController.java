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
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.*;
import jodd.util.StringPool;
import lombok.AllArgsConstructor;
import org.nodes.core.base.entity.Menu;
import org.nodes.core.base.entity.TopMenu;
import org.nodes.core.base.service.IMenuService;
import org.nodes.core.base.service.ITopMenuService;
import org.nodes.core.base.vo.CheckedTreeVO;
import org.nodes.core.base.vo.GrantTreeVO;
import org.nodes.core.base.vo.MenuVO;
import org.nodes.core.base.wrapper.MenuWrapper;
import org.nodes.core.constant.WmsAppConstant;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.support.Kv;
import org.springblade.core.tool.utils.Func;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springblade.core.cache.constant.CacheConstant.MENU_CACHE;

/**
 * ?????????
 *
 * @author Nodes
 */
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_SYSTEM_NAME + "/menu")
@Api(value = "??????", tags = "??????")
public class MenuController extends BladeController {

	private IMenuService menuService;
	private ITopMenuService topMenuService;

	/**
	 * ??????
	 */
	@GetMapping("/detail")
	@ApiOperation(value = "??????", notes = "??????menu")
	public R<MenuVO> detail(Menu menu) {
		return R.data(MenuWrapper.build().entityVO(menuService.getOne(Condition.getQueryWrapper(menu))));
	}

	/**
	 * ??????
	 */
	@GetMapping("/list")
	@ApiOperation(value = "??????", notes = "??????menu")
	public R<List<MenuVO>> list(@ApiIgnore @RequestParam Map<String, Object> params) {
		List<Menu> list = menuService.list(Condition.getQueryWrapper(params, Menu.class)).stream().filter(u -> {
			if (!SecureUtil.isDeveloper()) {
				return u.getIsVisible().equals(0);
			}
			return true;
		}).sorted(new Comparator<Menu>() {
			@Override
			public int compare(Menu o1, Menu o2) {
				return Func.isNotEmpty(o1.getSort()) ? o1.getSort().compareTo(o2.getSort()) : 0;
			}
		}).collect(Collectors.toList());
		List<MenuVO> menuVOList = MenuWrapper.build().listVO(list);
		List<Menu> menuList = menuService.list(Condition.getQueryWrapper(new Menu()).lambda()
			.func(sql -> {
				if (!SecureUtil.isDeveloper()) {
					sql.eq(Menu::getIsVisible, 0);
				}
			}));
		for (MenuVO item : menuVOList) {
			Long count = menuList.stream().filter(u -> {
				boolean category = true;
				if (params.containsKey("category")) {
					category = params.getOrDefault("category", StringPool.EMPTY).equals(u.getCategory());
				}
				return category && u.getParentId().equals(item.getId());
			}).count();
			item.setHasChildren(count > 0);
		}

		return R.data(menuVOList);
	}

	@GetMapping("/page")
	public R<IPage<MenuVO>> page(@ApiIgnore @RequestParam Map<String, Object> params, Query query) {
		LambdaQueryWrapper<Menu> queryWrapper = Condition.getQueryWrapper(params, Menu.class)
			.lambda()
			.eq(Menu::getParentId, WmsAppConstant.TOP_PARENT_ID)
			.func(sql -> {
				if (!SecureUtil.isDeveloper()) {
					sql.eq(Menu::getIsVisible, 0);
				}
			})
			.orderByAsc(Menu::getSort);
		IPage<Menu> page = menuService.page(Condition.getPage(query), queryWrapper);
		List<Menu> menuList = menuService.list(Condition.getQueryWrapper(new Menu()).lambda()
			.func(sql -> {
				if (!SecureUtil.isDeveloper()) {
					sql.eq(Menu::getIsVisible, 0);
				}
			}));
		IPage<MenuVO> menuPage = MenuWrapper.build().pageVO(page);
		if (Func.isNotEmpty(menuPage) && Func.isNotEmpty(menuPage.getRecords())) {
			menuPage.getRecords().forEach(parent -> {
				Long count = menuList.stream().filter(u -> {
					return u.getParentId().equals(parent.getId());
				}).count();
				parent.setHasChildren(count > 0);
			});
		}
		return R.data(menuPage);
	}

	/**
	 * ??????
	 */
	@GetMapping("/menu-list")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "code", value = "????????????", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "name", value = "????????????", paramType = "query", dataType = "string")
	})
	@ApiOperation(value = "??????", notes = "??????menu")
	public R<List<MenuVO>> menuList(@ApiIgnore @RequestParam Map<String, Object> menu) {
		List<Menu> list = menuService.list(Condition.getQueryWrapper(menu, Menu.class)
			.lambda()
			.eq(Menu::getAlias, "menu")
			.orderByAsc(Menu::getSort));
		return R.data(MenuWrapper.build().listNodeVO(list));
	}

	/**
	 * ???????????????
	 */
	@ApiLog("??????-???????????????")
	@PostMapping("/submit")
	@CacheEvict(cacheNames = {MENU_CACHE}, allEntries = true)
	@ApiOperation(value = "???????????????", notes = "??????menu")
	public R submit(@Valid @RequestBody Menu menu) {
		CacheUtil.clear(MENU_CACHE);
		return R.status(menuService.submit(menu));
	}


	/**
	 * ??????
	 */
	@ApiLog("??????-??????")
	@PostMapping("/remove")
	@CacheEvict(cacheNames = {MENU_CACHE}, allEntries = true)
	@ApiOperation(value = "??????", notes = "??????ids")
	public R remove(@ApiParam(value = "????????????", required = true) @RequestParam String ids) {
		CacheUtil.clear(MENU_CACHE);
		return R.status(menuService.removeMenu(ids));
	}

	/**
	 * ??????
	 */
	@ApiLog("??????-??????????????????")
	@GetMapping("/change-visible")
	@ApiOperation(value = "??????????????????", notes = "??????id,type")
	public R changeVisible(Long id, Integer type) {
		CacheUtil.clear(MENU_CACHE);
		return R.status(menuService.changeVisible(id, type));
	}

	/**
	 * ??????????????????
	 */
	@GetMapping("/routes")
	@ApiOperation(value = "??????????????????", notes = "??????????????????")
	public R<List<MenuVO>> routes(BladeUser user, Long topMenuId, Integer systemType) {
		List<MenuVO> list = menuService.routes((user == null) ? null : user.getRoleId(), topMenuId, systemType);
		return R.data(list);
	}

	/**
	 * ??????????????????
	 */
	@GetMapping("/routes-ext")
	@ApiOperation(value = "??????????????????", notes = "??????????????????")
	public R<List<MenuVO>> routesExt(BladeUser user, Long topMenuId, Integer systemType) {
		List<MenuVO> list = menuService.routesExt(user.getRoleId(), topMenuId, systemType);
		return R.data(list);
	}

	/**
	 * ??????????????????
	 */
	@GetMapping("/buttons")
	@ApiOperation(value = "??????????????????", notes = "??????????????????")
	public R<List<MenuVO>> buttons(@NotNull BladeUser user) {
		List<MenuVO> list = menuService.buttons(user.getRoleId());
		return R.data(list);
	}

	/**
	 * ????????????????????????
	 */
	@GetMapping("/tree")
	@ApiOperation(value = "????????????", notes = "????????????")
	public R<List<MenuVO>> tree() {
		List<MenuVO> tree = menuService.tree();
		return R.data(tree);
	}

	/**
	 * ??????????????????????????????
	 */
	@GetMapping("/grant-tree")
	@ApiOperation(value = "????????????????????????", notes = "????????????????????????")
	public R<GrantTreeVO> grantTree(BladeUser user) {
		GrantTreeVO vo = new GrantTreeVO();
		vo.setMenu(menuService.grantTree(user));
		vo.setDataScope(menuService.grantDataScopeTree(user));
		vo.setApiScope(menuService.grantApiScopeTree(user));
		return R.data(vo);
	}

	/**
	 * ??????????????????????????????
	 */
	@GetMapping("/role-tree-keys")
	@ApiOperation(value = "?????????????????????", notes = "?????????????????????")
	public R<CheckedTreeVO> roleTreeKeys(String roleIds) {
		CheckedTreeVO vo = new CheckedTreeVO();
		vo.setMenu(menuService.roleTreeKeys(roleIds));
		vo.setDataScope(menuService.dataScopeTreeKeys(roleIds));
		vo.setApiScope(menuService.apiScopeTreeKeys(roleIds));
		return R.data(vo);
	}

	/**
	 * ??????????????????????????????
	 */
	@GetMapping("/grant-top-tree")
	@ApiOperation(value = "????????????????????????", notes = "????????????????????????")
	public R<GrantTreeVO> grantTopTree(BladeUser user) {
		GrantTreeVO vo = new GrantTreeVO();
		vo.setMenu(menuService.grantTopTree(user));
		return R.data(vo);
	}

	/**
	 * ??????????????????????????????
	 */
	@GetMapping("/top-tree-keys")
	@ApiOperation(value = "???????????????????????????", notes = "???????????????????????????")
	public R<CheckedTreeVO> topTreeKeys(String topMenuIds) {
		CheckedTreeVO vo = new CheckedTreeVO();
		vo.setMenu(menuService.topTreeKeys(topMenuIds));
		return R.data(vo);
	}

	/**
	 * ??????????????????
	 */
	@GetMapping("/top-menu")
	@ApiOperation(value = "??????????????????", notes = "??????????????????")
	public R<List<TopMenu>> topMenu(BladeUser user) {
		if (Func.isEmpty(user)) {
			return null;
		}
		List<TopMenu> list = topMenuService.list().stream().sorted(new Comparator<TopMenu>() {
			@Override
			public int compare(TopMenu o1, TopMenu o2) {
				return o1.getSort().compareTo(o2.getSort());
			}
		}).collect(Collectors.toList());
		return R.data(list);
	}

	/**
	 * ???????????????????????????
	 */
	@GetMapping("auth-routes")
	@ApiOperation(value = "?????????????????????")
	public R<List<Kv>> authRoutes(BladeUser user) {
		if (Func.isEmpty(user)) {
			return null;
		}
		return R.data(menuService.authRoutes(user));
	}

}
