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
package org.nodes.wms.core.outstock.loading.service.impl;

import org.nodes.wms.core.outstock.loading.entity.TruckSerial;
import org.nodes.wms.core.outstock.loading.mapper.TruckSerialMapper;
import org.nodes.wms.core.outstock.loading.service.ITruckSerialService;
import org.nodes.wms.core.outstock.loading.vo.TruckSerialVO;
import org.nodes.wms.core.outstock.loading.wrapper.TruckSerialWrapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 装车发货序列号日志 服务实现类
 *
 * @author pengwei
 * @since 2020-04-10
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class TruckSerialServiceImpl<M extends TruckSerialMapper, T extends TruckSerial>
	extends BaseServiceImpl<TruckSerialMapper, TruckSerial>
	implements ITruckSerialService {


	@Override
	public List<TruckSerialVO> listBySystemProcId(Long systemProcId) {
		TruckSerial truckSerial = new TruckSerial();
		truckSerial.setSystemProcId(systemProcId);
		List<TruckSerial> list = super.list(Condition.getQueryWrapper(truckSerial));

		return TruckSerialWrapper.build().listVO(list);
	}

}
