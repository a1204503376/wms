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


import com.sun.xml.bind.v2.model.core.ID;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.base.service.ITenantService;
import org.nodes.core.base.vo.DeptVO;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.SysCache;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.node.INode;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
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
public class DeptWrapper extends BaseEntityWrapper<Dept, DeptVO> {

	public static DeptWrapper build() {
		return new DeptWrapper();
	}

	@Override
	public DeptVO entityVO(Dept dept) {
		DeptVO deptVO = Objects.requireNonNull(BeanUtil.copy(dept, DeptVO.class));
		if (Func.equals(dept.getParentId(), BladeConstant.TOP_PARENT_ID)) {
			deptVO.setParentName(BladeConstant.TOP_PARENT_NAME);
		} else {
			Dept parent = SysCache.getDept(dept.getParentId());
			deptVO.setParentName(parent.getDeptName());
		}
		String category = DictCache.getValue("org_category", dept.getDeptCategory());
		deptVO.setDeptCategoryName(category);
		return deptVO;
	}


	public List<INode> listNodeVO(List<Dept> list) {
		List<INode> collect = list.stream().map(dept -> {
			DeptVO deptVO = BeanUtil.copy(dept, DeptVO.class);
			String category = DictCache.getValue("org_category", dept.getDeptCategory());
			Objects.requireNonNull(deptVO).setDeptCategoryName(category);
			if (Func.isNotEmpty(deptVO.getTenantId())) {
				ITenantService tenantService = SpringUtil.getBean(ITenantService.class);
				List<String> tenantNames = tenantService.getTenantNames(deptVO.getTenantId());
				if (tenantNames.size() > 0) {
					deptVO.setTenantName(tenantNames.get(0));
				}
			}
			return deptVO;
		}).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}
}
