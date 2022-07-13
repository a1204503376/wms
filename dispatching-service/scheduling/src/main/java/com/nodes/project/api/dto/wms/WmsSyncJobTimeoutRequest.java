package com.nodes.project.api.dto.wms;

import lombok.Data;

/**
 * 同步JOB超时信息给WMS的请求对象
 */
@Data
public class WmsSyncJobTimeoutRequest {

    /**
     * WMS的任务明细ID
     */
    private Long taskDetailId;

    /**
     * WMS的任务头表ID
     */
    private Long taskHeaderId;


}
