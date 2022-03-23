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

import com.sun.xml.bind.v2.model.core.ID;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.ApiScope;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.base.vo.ApiScopeVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;


/**
 * 包装类,返回视图层所需的字段
 *
 * @author Nodes
 */
public class ApiScopeWrapper extends BaseEntityWrapper<ApiScope, ApiScopeVO> {

	public static ApiScopeWrapper build() {
		return new ApiScopeWrapper();
	}

	@Override
	public ApiScopeVO entityVO(ApiScope dataScope) {
		ApiScopeVO apiScopeVO = BeanUtil.copy(dataScope, ApiScopeVO.class);
		if (Func.isNotEmpty(apiScopeVO)) {
			apiScopeVO.setScopeTypeName(DictCache.getValue("api_scope_type", dataScope.getScopeType()));
		}
		return apiScopeVO;
	}

}
