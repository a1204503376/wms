/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
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
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.nodes.wms.core.outstock.loading.service;

import org.nodes.wms.core.outstock.loading.entity.TruckSerial;
import org.nodes.wms.core.outstock.loading.vo.TruckSerialVO;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 装车发货序列号日志 服务类
 *
 * @author pengwei
 * @since 2020-04-10
 */
public interface ITruckSerialService extends BaseService<TruckSerial> {

	/**
	 * 根据 系统日志ID 获取所有关联的装车发货序列号信息
	 *
	 * @param systemProcId 系统日志ID
	 * @return 装车发货序列号集合
	 */
	List<TruckSerialVO> listBySystemProcId(Long systemProcId);
}
