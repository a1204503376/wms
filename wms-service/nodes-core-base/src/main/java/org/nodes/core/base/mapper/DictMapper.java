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
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.vo.DictVO;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author Nodes
 */
@Primary
public interface DictMapper extends BaseMapper<Dict> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param dict
	 * @return
	 */
	List<DictVO> selectDictPage(IPage page, DictVO dict);

	/**
	 * 获取字典表对应中文
	 *
	 * @param code    字典编号
	 * @param dictKey 字典序号
	 * @return
	 */
	String getValue(String code, Integer dictKey);

	/**
	 * 获取字典表对应中文
	 *
	 * @param code    字典编号
	 * @param dictKey 字典序号
	 * @return
	 */
	Dict getObj(String code, Integer dictKey);

	/**
	 * 获取字典表
	 *
	 * @param code 字典编号
	 * @return
	 */
	List<Dict> getList(String code);

	/**
	 * 获取树形节点
	 *
	 * @param owner 使用者
	 * @return
	 */
	List<DictVO> tree(String owner);

	/**
	 * 逻辑删除
	 * @param id 主键id
	 * @return
	 */
	Integer deleteById(Long id);

}
