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
package org.nodes.core.base.cache;

/**
 * 缓存名
 *
 * @author Nodes
 */
public interface CacheNames {

	String CAPTCHA_KEY = "blade:auth::blade:captcha:";
	/**
	 * 临时缓存
	 */
	String NODES_FLASH = "nodes:flash";

	/**
	 * 机构缓存名称
	 */
	String NODE_DEPT = "node:dept";
}
