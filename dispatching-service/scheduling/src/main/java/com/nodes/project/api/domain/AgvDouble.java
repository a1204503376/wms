package com.nodes.project.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nodes.framework.web.domain.SimpleEntity;
import lombok.Data;

/**
 * AGV双重入库信息
 */
@TableName(value = "agv_double")
@Data
public class AgvDouble extends SimpleEntity {
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
