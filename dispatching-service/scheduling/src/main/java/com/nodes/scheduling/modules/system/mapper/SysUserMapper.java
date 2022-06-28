package com.nodes.scheduling.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nodes.scheduling.modules.system.entity.SysUser;

/**
 *
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser selectByName(String account);
}