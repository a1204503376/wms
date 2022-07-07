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
package org.nodes.core.base.wrapper;

import org.nodes.core.base.service.ITenantService;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.core.base.cache.*;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.entity.Tenant;
import org.nodes.core.base.vo.DictVO;
import org.springblade.core.tool.utils.SpringUtil;


/**
 * 包装类,返回视图层所需的字段
 *
 * @author Nodes
 */
public class DictWrapper extends BaseEntityWrapper<Dict, DictVO> {

	public static DictWrapper build() {
		return new DictWrapper();
	}

	@Override
	public DictVO entityVO(Dict dict) {
		DictVO dictVO = BeanUtil.copy(dict, DictVO.class);
		if (Func.equals(dict.getParentId(), BladeConstant.TOP_PARENT_ID)) {
			dictVO.setParentName(BladeConstant.TOP_PARENT_NAME);
		} else {
			Dict parent = DictCache.getById(dict.getParentId());
			if (Func.isNotEmpty(parent)) {
				dictVO.setParentName(parent.getDictValue());
			}
		}
		ITenantService tenantService = SpringUtil.getBean(ITenantService.class);
		Tenant tenant = tenantService.getById(dictVO.getTenantId());
		if (Func.isNotEmpty(tenant)) {
			dictVO.setTenantName(tenant.getTenantName());
		}
		if(DictCodeConstant.QUALITY_TYPE.equals(dict.getCode())){
			dictVO.setQualityDateType(String.valueOf(dict.getDictKey()));
		}
		String visible = DictCache.getValue("is_visible", dictVO.getIsVisible());
		dictVO.setIsVisibleName(visible);
		return dictVO;
	}
}
