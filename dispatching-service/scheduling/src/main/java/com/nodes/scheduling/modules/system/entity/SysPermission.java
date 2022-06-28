package com.nodes.scheduling.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 权限表
 */
@Data
@TableName(value = "sys_permission")
public class SysPermission {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 权限code
     */
    @TableField(value = "permission_code")
    private String permissionCode;

    /**
     * 权限名
     */
    @TableField(value = "permission_name")
    private String permissionName;

    public static final String COL_ID = "id";

    public static final String COL_PERMISSION_CODE = "permission_code";

    public static final String COL_PERMISSION_NAME = "permission_name";
}