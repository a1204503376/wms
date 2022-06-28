package com.nodes.scheduling.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nodes.scheduling.modules.system.entity.SysPermission;

import java.util.List;

/**
 *
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    List<SysPermission> selectListByUserId(Integer userId);
}