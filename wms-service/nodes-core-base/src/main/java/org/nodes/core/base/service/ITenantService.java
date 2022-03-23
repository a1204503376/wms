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
import org.nodes.core.base.entity.Tenant;

import java.util.Date;
import java.util.List;

/**
 * 服务类
 *
 * @author Nodes
 */
public interface ITenantService extends BaseService<Tenant> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param tenant
	 * @return
	 */
	IPage<Tenant> selectTenantPage(IPage<Tenant> page, Tenant tenant);

	/**
	 * 根据租户编号获取实体
	 *
	 * @param tenantId
	 * @return
	 */
	Tenant getByTenantId(String tenantId);

	/**
	 * 新增
	 *
	 * @param tenant
	 * @return
	 */
	boolean submitTenant(Tenant tenant);

	/**
	 * 获取租户名
	 *
	 * @param tenantIds
	 * @return
	 */
	List<String> getTenantNames(String tenantIds);

	/**
	 * 删除
	 *
	 * @param ids
	 * @return
	 */
	boolean removeTenant(List<Long> ids);

	/**
	 * 配置租户授权
	 *
	 * @param accountNumber
	 * @param expireTime
	 * @param whMax
	 * @param ids
	 * @return
	 */
	boolean setting(Integer accountNumber, Date expireTime, Integer whMax, String ids);
}
