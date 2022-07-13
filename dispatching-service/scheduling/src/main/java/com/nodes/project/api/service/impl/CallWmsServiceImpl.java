package com.nodes.project.api.service.impl;

import com.nodes.framework.config.NodesConfig;
import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.dto.wms.WmsGlobalResponse;
import com.nodes.project.api.dto.wms.WmsOutboundResourcesRequest;
import com.nodes.project.api.dto.wms.WmsSyncTaskStateRequest;
import com.nodes.project.api.service.CallApiService;
import com.nodes.project.api.service.CallWmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class CallWmsServiceImpl implements CallWmsService {

    private static final String url_queryAndFrozenEnableOutbound = "/wms/scheduling/queryAndFrozenEnableOutbound";
    private static final String url_syncTaskSate = "/wms/scheduling/syncTaskState";

    @Resource
    private CallApiService callApiService;
    @Resource
    private NodesConfig nodesConfig;

    /**
     * 同步AGV执行状态给WMS
     */
    @Async
    @Override
    public void syncTaskState(JobQueue jobQueue) {
        WmsSyncTaskStateRequest wmsSyncTaskStateRequest = new WmsSyncTaskStateRequest();
        wmsSyncTaskStateRequest.setTaskHeaderId(jobQueue.getWmsTaskId());
        wmsSyncTaskStateRequest.setTaskDetailId(jobQueue.getWmsTaskDetailId());
        wmsSyncTaskStateRequest.setState(jobQueue.getStatus());
        String url = nodesConfig.getWmsUrl() + url_syncTaskSate;
        callApiService.postWms(url, wmsSyncTaskStateRequest);
    }

    @Override
    public WmsGlobalResponse queryAndFrozenEnableOutbound(JobQueue jobQueue) {
        WmsOutboundResourcesRequest wmsOutboundResourcesRequest = new WmsOutboundResourcesRequest();
        wmsOutboundResourcesRequest.setTaskDetailId(jobQueue.getWmsTaskDetailId());
        wmsOutboundResourcesRequest.setLpnTypeCode(jobQueue.getWmsBoxType().getCode());
        String url = nodesConfig.getWmsUrl() + url_queryAndFrozenEnableOutbound;
        return callApiService.postWms(url, wmsOutboundResourcesRequest);
    }
}
