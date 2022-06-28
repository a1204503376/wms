package com.nodes.scheduling.modules.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nodes.scheduling.modules.system.entity.SysPermission;
import com.nodes.scheduling.modules.system.mapper.SysPermissionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class SysPermissionService extends ServiceImpl<SysPermissionMapper, SysPermission> {

    /**
     * 查询用户的权限列表
     */
    public List<SysPermission> selectListByUserId(Integer userId) {
        return baseMapper.selectListByUserId(userId);
    }

}
