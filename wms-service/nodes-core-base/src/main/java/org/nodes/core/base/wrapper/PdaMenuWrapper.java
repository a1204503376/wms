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

import org.nodes.core.base.cache.MenuCache;
import org.nodes.core.base.entity.Menu;
import org.nodes.core.base.vo.PdaMenuVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author Nodes
 */
public class PdaMenuWrapper extends BaseEntityWrapper<Menu, PdaMenuVO> {

	public static PdaMenuWrapper build() {
		return new PdaMenuWrapper();
	}

	@Override
	public PdaMenuVO entityVO(Menu menu) {
		PdaMenuVO menuVO = BeanUtil.copy(menu, PdaMenuVO.class);
		if (Func.equals(menu.getParentId(), BladeConstant.TOP_PARENT_ID)) {
			menuVO.setParentName(BladeConstant.TOP_PARENT_NAME);
		} else {
			Menu parent = MenuCache.getById(menu.getParentId());
			if (Func.isNotEmpty(parent)) {
				menuVO.setParentName(parent.getName());
			}
		}
		return menuVO;
	}

	public List<PdaMenuVO> listNodeVO(List<Menu> list) {
		List<PdaMenuVO> collect = list.stream().map(menu -> entityVO(menu)).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}

}
