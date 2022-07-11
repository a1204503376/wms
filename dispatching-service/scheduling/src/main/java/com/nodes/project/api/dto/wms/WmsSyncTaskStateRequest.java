package com.nodes.project.api.dto.wms;

import com.nodes.project.api.enums.JobStatusEnum;
import lombok.Data;

/**
 * 同步给WMS的任务状态请求对象
 */
@Data
public class WmsSyncTaskStateRequest {

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
}
