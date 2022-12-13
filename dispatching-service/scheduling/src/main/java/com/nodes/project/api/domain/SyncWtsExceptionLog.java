package com.nodes.project.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.nodes.project.api.enums.JobStatusEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 同步wms任务状态异常日志实体类
 **/
@TableName(value = "sync_wts_exception_log")
@Data
public class SyncWtsExceptionLog {

    /**
     * 异常日志id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 同步的状态
     */
    private JobStatusEnum state;

    /**
     * WMS的任务明细ID
     */
    private Long taskDetailId;

    /**
     * WMS的任务头表ID
     */
    private Long taskHeaderId;

    /**
     * 消息或异常信息
     */
    private String msg;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
