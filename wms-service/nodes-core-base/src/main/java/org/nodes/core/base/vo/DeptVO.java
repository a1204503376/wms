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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.tool.node.INode;
import org.nodes.core.base.entity.Dept;

import java.util.ArrayList;
import java.util.List;

/**
 * 视图实体类
 *
 * @author Nodes
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "DeptVO对象", description = "DeptVO对象")
public class DeptVO extends Dept implements INode {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 父节点ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentId;

	/**
	 * 子孙节点
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<INode> children;

	/**
	 * 租户名称
	 */
	@ApiModelProperty(value = "租户名称")
	private String tenantName;

	private String title;
	private String value;
	private String key;

	@Override
	public List<INode> getChildren() {
		if (this.children == null) {
			this.children = new ArrayList<>();
		}
		return this.children;
	}

	/**
	 * 是否有子节点
	 * @return
	 */
	public boolean hasChildren;

	/**
	 * 上级机构
	 */
	private String parentName;

	/**
	 * 机构类型名称
	 */
	private String deptCategoryName;
	private String uuid;
}
