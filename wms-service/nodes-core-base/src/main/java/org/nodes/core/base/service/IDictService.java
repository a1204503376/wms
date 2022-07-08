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
import org.springblade.core.mp.base.BaseService;
import org.nodes.core.base.dto.DictDTO;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.vo.DictVO;

import java.util.List;

/**
 * 服务类
 *
 * @author Nodes
 */
public interface IDictService extends BaseService<Dict> {

	/**
	 * 树形结构
	 *
	 * @return
	 */
	List<DictVO> tree();

	/**
	 * 新增或修改
	 *
	 * @param dict
	 * @return
	 */
	boolean submit(Dict dict);

	/**
	 * 删除字典
	 *
	 * @param ids
	 * @return
	 */
	boolean removeDict(String ids);

	/**
	 * 控制字典显隐
	 *
	 * @param id
	 * @param type
	 * @return
	 */
	boolean changeVisible(Long id, Integer type);

	/**
	 * 根据字典code获取子级
	 * @param dictCode
	 * @return
	 */
	List<Dict> findSonDictByDictCode(String dictCode);

	/**
	 * 根据字典value获取字典
	 * @param dictCode
	 * @param dictValue
	 * @return
	 */
	Dict findByDictValue(String dictCode, String dictValue);

	/**
	 * 根据字典key获取字典
	 * @param dictCode
	 * @param dictKey
	 * @return
	 */
	Dict findByDictKey(String dictCode, String dictKey);
}
