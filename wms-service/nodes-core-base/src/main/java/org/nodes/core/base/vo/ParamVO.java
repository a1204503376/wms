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
package org.nodes.core.base.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.core.base.entity.Param;

/**
 * 视图实体类
 *
 * @author Nodes
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ParamVO对象", description = "ParamVO对象")
public class ParamVO extends Param {
	private static final long serialVersionUID = 1L;

	/**
	 * 租户名称
	 */
	private String  tenantName;
	/**
	 * 是否显示该参数描述
	 */
	private String isVisibleName;
}
