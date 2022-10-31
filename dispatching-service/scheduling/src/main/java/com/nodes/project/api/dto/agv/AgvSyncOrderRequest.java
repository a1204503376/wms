package com.nodes.project.api.dto.agv;

import com.alibaba.fastjson.annotation.JSONField;
import com.nodes.project.api.enums.AgvTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * AGV回调更新JOB状态请求对象
 */
@Data
public class AgvSyncOrderRequest {

    /**
     * 调度系统的JOB_ID
     */
    @NotBlank(message = "调度系统的JOB_ID编码不能为空")
    private String jobId;

    /**
     * AGV通知类型
     */
    @NotNull(message = "AGV通知类型不能为空")
    @JSONField(serializeUsing = AgvTypeEnumSerializer.class)
    private AgvTypeEnum agvType;

    /**
     * AGV车辆名称
     */
    @NotBlank(message = "AGV车辆名称不能为空")
    private String agvName;

    /**
     * 消息
     */
    private String msg;

    /**
     * 代码
     */
    private String code;
}
