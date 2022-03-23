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


import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.utils.TokenUtil;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springblade.core.cache.constant.CacheConstant.DICT_CACHE;

/**
 * 字典缓存工具类
 *
 * @author Nodes
 */
public class DictCache {

	final static String DICT_ID = "dict:id:";
	final static String DICT_CODE_KEY = "dict:code:key:";
	final static String DICT_LIST_CODE = "dict:list:code:";

	static IDictService dictService;

	static {
		dictService = SpringUtil.getBean(IDictService.class);
	}

	/**
	 * 获取字典实体
	 *
	 * @param id 主键
	 * @return
	 */
	public static Dict getById(Long id) {
		return CacheUtil.get(DICT_CACHE, DICT_ID, id, () -> dictService.getById(id));
	}

	/**
	 * 获取字典值
	 *
	 * @param code    字典编号
	 * @param dictKey 字典键
	 * @return
	 */
	public static Dict getOne(String code, Integer dictKey) {
		return CacheUtil.get(DICT_CACHE, DICT_CODE_KEY, code + StringPool.COLON + dictKey, () -> {
			return dictService.list(Condition.getQueryWrapper(new Dict())
				.lambda()
				.eq(Dict::getCode, code)
				.eq(Dict::getDictKey, dictKey))
				.stream()
				.findFirst()
				.orElse(null);
		});
	}

	/**
	 * 获取字典值
	 *
	 * @param code    字典编号
	 * @param dictKey 字典键
	 * @return
	 */
	public static String getValue(String code, Integer dictKey) {
		Dict dict = getOne(code, dictKey);
		return Func.isEmpty(dict) ? StringPool.EMPTY : dict.getDictValue();
	}

	/**
	 * 获取字典列表
	 *
	 * @param code 字典编号
	 * @return 字典列表
	 */
	public static List<Dict> list(String code) {
		return CacheUtil.get(DICT_CACHE, DICT_LIST_CODE, code, () -> {
			return dictService.list(Condition.getQueryWrapper(new Dict())
				.lambda()
				.eq(Dict::getCode, code));
		});
	}
}
