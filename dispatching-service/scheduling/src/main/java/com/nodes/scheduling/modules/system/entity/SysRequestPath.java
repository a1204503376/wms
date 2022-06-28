package com.nodes.scheduling.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 请求路径
 */
@Data
@TableName(value = "sys_request_path")
public class SysRequestPath {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 请求路径
     */
    @TableField(value = "url")
    private String url;

    /**
     * 路径描述
     */
    @TableField(value = "description")
    private String description;

    public static final String COL_ID = "id";

    public static final String COL_URL = "url";

    public static final String COL_DESCRIPTION = "description";
}