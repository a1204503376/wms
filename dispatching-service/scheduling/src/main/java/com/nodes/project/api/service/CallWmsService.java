package com.nodes.project.api.service;

import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.dto.wms.WmsOutboundResourcesRequest;
import com.nodes.project.api.dto.wms.WmsResultResponse;

/**
 * 呼叫WMS的API接口
 */
public interface CallWmsService {

    void syncTaskState(JobQueue jobQueue);

    WmsResultResponse queryAndFrozenEnableOutbound(WmsOutboundResourcesRequest wmsOutboundResourcesRequest);
}
