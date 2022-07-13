package com.nodes.project.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nodes.framework.web.domain.SimpleEntity;
import lombok.Data;

/**
 * agv_sync
 */
@TableName(value = "agv_sync")
@Data
public class AgvSync extends SimpleEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * JOB ID
     */
    private String jobId;

    /**
     * AGV同步类型
     */
    private String agvType;

    /**
     * AGV的车辆名称
     */
    private String agvName;

    /**
     * 消息
     */
    private String msg;


}