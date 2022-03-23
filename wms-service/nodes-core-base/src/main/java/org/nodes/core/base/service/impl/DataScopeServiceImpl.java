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

import lombok.AllArgsConstructor;
import org.nodes.core.base.mapper.UserMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.nodes.core.base.dto.DataScopeDTO;
import org.nodes.core.base.entity.DataScope;
import org.nodes.core.base.mapper.DataScopeMapper;
import org.nodes.core.base.service.IDataScopeService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *  服务实现类
 *
 * @author NodeX
 * @since 2019-06-23
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class DataScopeServiceImpl <M extends DataScopeMapper, T extends DataScope> extends BaseServiceImpl<DataScopeMapper, DataScope> implements IDataScopeService {

	@Override
	public Boolean submit(DataScopeDTO dataScopeDTO) {
		// 删除该菜单下所有数据权限
		super.remove(Condition.getQueryWrapper(new DataScope())
			.lambda()
			.eq(DataScope::getMenuId, dataScopeDTO.getMenuId()));
		// 存储数据权限
		for (DataScope dataScope : dataScopeDTO.getDataScopeList()) {
			dataScope.setMenuId(dataScopeDTO.getMenuId());
			super.save(dataScope);
		}
		return true;
	}
}
