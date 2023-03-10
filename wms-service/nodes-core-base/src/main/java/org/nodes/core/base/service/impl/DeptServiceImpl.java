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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.nodes.core.base.dto.DeptDTO;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.base.entity.User;
import org.nodes.core.base.mapper.DeptMapper;
import org.nodes.core.base.service.IDeptService;
import org.nodes.core.base.service.IUserService;
import org.nodes.core.base.vo.DeptVO;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringPool;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * ???????????????
 *
 * @author Nodes
 */
@Service
@AllArgsConstructor
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class DeptServiceImpl<M extends DeptMapper, T extends Dept> extends BaseServiceImpl<DeptMapper, Dept> implements IDeptService {

	IUserService userService;

	@Override
	public List<DeptVO> tree(String tenantId, Long id) {
		return ForestNodeMerger.merge(baseMapper.tree(tenantId, id));
	}

	@Override
	public List<Dept> list() {
		return super.list();
	}

	@Override
	public boolean save(Dept entity) {
		return super.save(entity);
	}

	@Override
	public List<String> getDeptNames(String deptIds) {
		return baseMapper.getDeptNames(Func.toLongArray(deptIds));
	}

	@Override
	public boolean submit(Dept dept) {
		if (Func.isEmpty(dept.getParentId())) {
			dept.setParentId(BladeConstant.TOP_PARENT_ID);
			dept.setAncestors(StringPool.ZERO);
		}
		if (dept.getParentId() > 0) {
			Dept parent = getById(dept.getParentId());
			String ancestors = parent.getAncestors() + StringPool.COMMA + dept.getParentId();
			dept.setAncestors(ancestors);
		}
		if (Func.isNotEmpty(dept.getDeptCode())) {
			QueryWrapper<Dept> deptQueryWrapper = new QueryWrapper<Dept>();
			if (Func.isNotEmpty(dept.getId())) {
				deptQueryWrapper.ne("id", dept.getId());
			}
			deptQueryWrapper.eq("dept_code", dept.getDeptCode());
			Integer cnt = baseMapper.selectCount(deptQueryWrapper);
			if (cnt > 0) {
				throw new ServiceException("?????????????????????");
			}
		}
		dept.setIsDeleted(BladeConstant.DB_NOT_DELETED);
		return saveOrUpdate(dept);
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		Integer cnt = baseMapper.selectCount(Wrappers.<Dept>query().lambda().in(Dept::getParentId, idList));
		if (cnt > 0) {
			throw new ServiceException("?????????????????????!");
		}
		// ?????????????????????????????????
		QueryWrapper<User> queryWrapper = Condition.getQueryWrapper(new User());
		queryWrapper.in("dept_id", idList);
		List<User> userList = userService.list(queryWrapper);
		if (Func.isNotEmpty(userList)) {
			// ????????????????????????????????????????????????
			for (User user : userList) {
				Dept dept = super.getById(Long.valueOf(user.getDeptId()));
				if (Func.isNotEmpty(dept)) {
					throw new ServiceException("?????????????????????????????????????????????????????????(???????????????" + dept.getDeptCode() + ")");
				} else {
					throw new ServiceException("?????????????????????????????????????????????????????????(??????ID???" + dept.getId() + ")");
				}
			}
		}
		idList.forEach(id -> {
			baseMapper.deleteById(id);
		});
		return true;
	}

	@Override
	public boolean updateById(DeptDTO deptDTO) {
		return super.updateById(deptDTO);
	}

	@Override
	public List<Dept> getAllChildDept(String deptId) {
		LambdaQueryWrapper<Dept> lambdaQueryWrapper = Wrappers.lambdaQuery(Dept.class);
		lambdaQueryWrapper.like(Dept::getAncestors, deptId);
		List<Dept> deptList = super.list(lambdaQueryWrapper);
		return deptList;
	}

}
