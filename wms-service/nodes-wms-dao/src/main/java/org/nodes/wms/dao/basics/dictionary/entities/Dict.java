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
package org.nodes.wms.dao.basics.dictionary.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

import java.io.Serializable;

/**
 * 字典 实体类
 */
@Data
@TableName("blade_dict")
public class Dict extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 父主键
	 */
	private Long parentId;

	/**
	 * 字典码
	 */
	private String code;

	/**
	 * 字典值
	 */
	private Integer dictKey;

	/**
	 * 字典名称
	 */
	private String dictValue;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 字典备注
	 */
	private String remark;

	/**
	 * 是否已封存
	 */
	private Integer isSealed;

	/**
	 * 是否已删除
	 */
	private Integer isDeleted;

	/**
	 * 是否显示该字典
	 */
	private Integer isVisible;

	/**
	 * 租户ID
	 */
	private String tenantId;
}