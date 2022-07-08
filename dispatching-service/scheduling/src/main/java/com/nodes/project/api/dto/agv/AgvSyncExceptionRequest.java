package com.nodes.project.api.dto.agv;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * AGV同步非运输过程中的异常请求对象
 */
@Data
public class AgvSyncExceptionRequest {

    /**
     * 调度系统的JOB_ID
     */
    private String jobId;

    /**
     * AGV车辆名称
     */
    private String agvName;

    /**
     * 异常编码
     */
    private String code;

    /**
     * 异常消息
     */
    @NotBlank(message = "异常消息不能为空")
    private String msg;
}
