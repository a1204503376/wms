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

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.MenuCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.base.service.IMenuService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.nodes.core.base.entity.Menu;
import org.nodes.core.base.vo.MenuVO;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author Nodes
 */
public class MenuWrapper extends BaseEntityWrapper<Menu, MenuVO> {

	public static MenuWrapper build() {
		return new MenuWrapper();
	}

	@Override
	public MenuVO entityVO(Menu menu) {
		MenuVO menuVO = BeanUtil.copy(menu, MenuVO.class);
		if (Func.equals(menu.getParentId(), BladeConstant.TOP_PARENT_ID)) {
			menuVO.setParentName(BladeConstant.TOP_PARENT_NAME);
		} else {
			Menu parent = MenuCache.getById(menu.getParentId());
			if (Func.isNotEmpty(parent)) {
				menuVO.setParentName(parent.getName());
			}
		}
		String category = DictCache.getValue("menu_category", Func.toInt(menuVO.getCategory()));
		String action = DictCache.getValue("button_func", Func.toInt(menuVO.getAction()));
		String open = DictCache.getValue("yes_no", Func.toInt(menuVO.getIsOpen()));
		String visible = DictCache.getValue("is_visible", Func.toInt(menuVO.getIsVisible()));
		menuVO.setSystemTypeName(DictCache.getValue("system_type", menuVO.getSystemType()));
		menuVO.setCategoryName(category);
		menuVO.setActionName(action);
		menuVO.setIsOpenName(open);
		menuVO.setIsVisibleName(visible);
		return menuVO;
	}

	public List<MenuVO> listNodeVO(List<Menu> list) {
		List<MenuVO> collect = list.stream().map(menu -> entityVO(menu)).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}

}
