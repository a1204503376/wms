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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * GrantVO
 *
 * @author Nodes
 */
@Data
public class GrantVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "roleIds集合")
	private List<Long> roleIds;

	@ApiModelProperty(value = "menuIds集合")
	private List<Long> menuIds;

	@ApiModelProperty(value = "dataScopeIds集合")
	private List<Long> dataScopeIds;

	@ApiModelProperty(value = "apiScopeIds集合")
	private List<Long> apiScopeIds;

	@ApiModelProperty(value = "topMenuIds集合")
	private List<Long> topMenuIds;

}
