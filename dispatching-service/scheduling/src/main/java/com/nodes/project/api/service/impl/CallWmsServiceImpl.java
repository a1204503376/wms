package com.nodes.project.api.service.impl;

import com.nodes.framework.config.NodesConfig;
import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.dto.wms.WmsOutboundResourcesRequest;
import com.nodes.project.api.dto.wms.WmsResultResponse;
import com.nodes.project.api.dto.wms.WmsSyncTaskStateRequest;
import com.nodes.project.api.service.CallWmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
@Slf4j
public class CallWmsServiceImpl implements CallWmsService {

    private static final String url_queryAndFrozenEnableOutbound = "/wms/scheduling/queryAndFrozenEnableOutbound";
    private static final String url_syncTaskSate = "/wms/scheduling/syncTaskState";

    @Resource
    private RestTemplate restTemplate;
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

        WmsResultResponse wmsResultResponse = restTemplate.postForObject(nodesConfig.getWmsUrl() + url_syncTaskSate, wmsSyncTaskStateRequest, WmsResultResponse.class);
        assert wmsResultResponse != null;
        if (BooleanUtils.isFalse(wmsResultResponse.getSuccess())) {
            log.error("WMS[{}]接口回失败，参数：{}", url_syncTaskSate, wmsSyncTaskStateRequest);
        }
    }

    @Override
    public WmsResultResponse queryAndFrozenEnableOutbound(WmsOutboundResourcesRequest wmsOutboundResourcesRequest) {
        return null;
    }
}
