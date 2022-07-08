package com.nodes.project.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 取消JOB的请求对象
 */
@Data
public class JobActionRequest {

    /**
     * WMS任务ID
     */
    @NotNull(message = "WMS任务ID不能为空")
    private Long wmsTaskId;

    /**
     * WMS任务明细ID
     */
    @NotNull(message = "WMS任务明细ID不能为空")
    private Long wmsTaskDetailId;
}
