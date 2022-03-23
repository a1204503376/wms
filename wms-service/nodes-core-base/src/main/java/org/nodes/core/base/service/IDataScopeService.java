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

import org.springblade.core.mp.base.BaseService;
import org.nodes.core.base.dto.DataScopeDTO;
import org.nodes.core.base.entity.DataScope;

/**
 * 服务类
 *
 * @author NodeX
 * @since 2019-06-23
 */
public interface IDataScopeService extends BaseService<DataScope> {

	/**
	 * 数据权限提交
	 *
	 * @param dataScopeDTO
	 * @return
	 */
	Boolean submit(DataScopeDTO dataScopeDTO);
}
