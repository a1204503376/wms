package com.nodes.project.api.dto.wms;

import lombok.Data;

/**
 * WMS出库接驳区库位资源请求对象
 */
@Data
public class WmsOutboundResourcesRequest {

    /**
     * 容器编码
     * A/B/C/D 箱
     */
    private String lpnTypeCode;

    /**
     * WMS的任务明细ID
     */
    private Long taskDetailId;
}
