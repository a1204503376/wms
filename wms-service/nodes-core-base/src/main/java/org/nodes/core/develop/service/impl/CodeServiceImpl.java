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
package org.nodes.core.develop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nodes.core.base.mapper.UserMapper;
import org.nodes.core.develop.entity.Code;
import org.nodes.core.develop.mapper.CodeMapper;
import org.nodes.core.develop.service.ICodeService;
import org.springblade.core.tool.constant.BladeConstant;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
@Primary
public class CodeServiceImpl <M extends CodeMapper, T extends Code> extends ServiceImpl<CodeMapper, Code> implements ICodeService {

	@Override
	public boolean submit(Code code) {
		code.setIsDeleted(BladeConstant.DB_NOT_DELETED);
		return saveOrUpdate(code);
	}

}
