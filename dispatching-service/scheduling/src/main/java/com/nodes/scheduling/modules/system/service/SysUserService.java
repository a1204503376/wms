package com.nodes.scheduling.modules.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nodes.scheduling.modules.system.entity.SysUser;
import com.nodes.scheduling.modules.system.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    /**
     * 根据用户名查询用户
     */
    public SysUser selectByName(String userName) {
        return baseMapper.selectByName(userName);
    }

}
