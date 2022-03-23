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
package org.nodes.core.base.service.impl;

import lombok.AllArgsConstructor;
import org.nodes.core.base.mapper.UserMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.nodes.core.base.entity.AuthClient;
import org.nodes.core.base.mapper.AuthClientMapper;
import org.nodes.core.base.service.IAuthClientService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *  服务实现类
 *
 * @author Nodes
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class AuthClientServiceImpl <M extends AuthClientMapper, T extends AuthClient> extends BaseServiceImpl<AuthClientMapper, AuthClient> implements IAuthClientService {

}
