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
package org.nodes.core.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.base.vo.DeptVO;
import org.springblade.core.datascope.annotation.DataAuth;
import org.springblade.core.datascope.enums.DataScopeEnum;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author Nodes
 */
@Primary
public interface DeptMapper extends BaseMapper<Dept> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param dept
	 * @return
	 */
	List<DeptVO> selectDeptPage(IPage page, DeptVO dept);

	/**
	 * 获取树形节点
	 *
	 * @param tenantId
	 * @return
	 */
	@DataAuth(type = DataScopeEnum.OWN_DEPT_CHILD,column = "id")
	List<DeptVO> tree(String tenantId, Long id);

	/**
	 * 获取机构名
	 *
	 * @param ids
	 * @return
	 */
	List<String> getDeptNames(Long[] ids);
}
