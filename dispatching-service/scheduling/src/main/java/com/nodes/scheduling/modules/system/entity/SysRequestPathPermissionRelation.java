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
    * 路径权限关联表
    */
@Data
@TableName(value = "sys_request_path_permission_relation")
public class SysRequestPathPermissionRelation {
    /**
     * 主键id
     */
    @TableField(value = "id")
    private Integer id;

    /**
     * 请求路径id
     */
    @TableField(value = "url_id")
    private Integer urlId;

    /**
     * 权限id
     */
    @TableField(value = "permission_id")
    private Integer permissionId;

    public static final String COL_ID = "id";

    public static final String COL_URL_ID = "url_id";

    public static final String COL_PERMISSION_ID = "permission_id";
}