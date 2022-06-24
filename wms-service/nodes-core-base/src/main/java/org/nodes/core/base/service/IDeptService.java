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

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.base.dto.DeptDTO;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.base.vo.DeptVO;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 服务类
 *
 * @author Nodes
 */
public interface IDeptService extends BaseService<Dept> {

	/**
	 * 树形结构
	 *
	 * @param tenantId
	 * @return
	 */
	List<DeptVO> tree(String tenantId, Long id);

	/**
	 * 获取部门名
	 *
	 * @param deptIds
	 * @return
	 */
	List<String> getDeptNames(String deptIds);

	/**
	 * 提交
	 *
	 * @param dept
	 * @return
	 */
	boolean submit(Dept dept);

	/**
	 * 修改部门
	 * @param deptDTO
	 * @return
	 */
	boolean updateById(DeptDTO deptDTO);

	/**
	 * 获取所有的下属部门,不含本部门
	 * @param deptId
	 * @return
	 */
	List<Dept> getAllChildDept(String deptId);

}
