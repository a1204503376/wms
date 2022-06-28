package com.nodes.scheduling.common.web.dao;

import java.time.LocalDateTime;

/**
 * 表结构基类
 */
public class BaseEntity {

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
