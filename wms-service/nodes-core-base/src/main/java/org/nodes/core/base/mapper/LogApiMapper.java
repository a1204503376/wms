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
package org.nodes.core.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springblade.core.log.model.LogApi;
import org.springframework.context.annotation.Primary;

/**
 * Mapper 接口
 *
 * @author Nodes
 */
@Primary
public interface LogApiMapper extends BaseMapper<LogApi> {

}
