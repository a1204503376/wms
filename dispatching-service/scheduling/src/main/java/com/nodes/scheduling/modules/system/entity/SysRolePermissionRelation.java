package com.nodes.scheduling.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
  *
  */
/**
    * 角色-权限关联关系表
    */
@Data
@TableName(value = "sys_role_permission_relation")
public class SysRolePermissionRelation {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 角色id
     */
    @TableField(value = "role_id")
    private Integer roleId;

    /**
     * 权限id
     */
    @TableField(value = "permission_id")
    private Integer permissionId;

    public static final String COL_ID = "id";

    public static final String COL_ROLE_ID = "role_id";

    public static final String COL_PERMISSION_ID = "permission_id";
}