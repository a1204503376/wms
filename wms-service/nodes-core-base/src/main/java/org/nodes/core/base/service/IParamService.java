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
import org.nodes.core.base.entity.Param;
import org.nodes.core.base.vo.ParamVO;

/**
 * 服务类
 *
 * @author Nodes
 */
public interface IParamService extends BaseService<Param> {

	/**
	 * 控制参数显隐
	 * @param id
	 * @param type
	 * @return
	 */
	boolean changeVisible(Long id, Integer type);

}
