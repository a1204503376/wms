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
package org.nodes.core.base.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nodes.core.base.entity.UserDept;
import org.nodes.core.base.mapper.UserDeptMapper;
import org.nodes.core.base.mapper.UserMapper;
import org.nodes.core.base.service.IUserDeptService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
@Primary
public class UserDeptServiceImpl <M extends UserDeptMapper, T extends UserDept> extends ServiceImpl<UserDeptMapper, UserDept> implements IUserDeptService {

}
