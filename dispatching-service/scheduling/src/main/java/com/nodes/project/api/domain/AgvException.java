package com.nodes.project.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nodes.framework.web.domain.SimpleEntity;
import lombok.Data;

/**
 * @TableName agv_exception
 */
@TableName(value = "agv_exception")
@Data
public class AgvException extends SimpleEntity {
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
     * AGV的车辆名称
     */
    private String agvName;

    /**
     * 异常编码
     */
    private String code;

    /**
     * 异常消息
     */
    private String msg;

}