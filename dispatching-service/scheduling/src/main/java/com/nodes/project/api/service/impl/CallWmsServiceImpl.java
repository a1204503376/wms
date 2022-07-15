package com.nodes.project.api.service.impl;

import com.nodes.common.utils.StringUtils;
import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.domain.JobTimeout;
import com.nodes.project.api.dto.wms.WmsGlobalResponse;
import com.nodes.project.api.dto.wms.WmsOutboundResourcesRequest;
import com.nodes.project.api.dto.wms.WmsSyncJobTimeoutRequest;
import com.nodes.project.api.dto.wms.WmsSyncTaskStateRequest;
import com.nodes.project.api.service.CallApiService;
import com.nodes.project.api.service.CallWmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CallWmsServiceImpl implements CallWmsService {

    private static final String url_queryAndFrozenEnableOutbound = "/queryAndFrozenEnableOutbound";
    private static final String url_syncTaskSate = "/syncTaskState";
    private static final String url_broadcastNotification = "/broadcastNotification";


    @Resource
    private CallApiService callApiService;

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
        callApiService.postWms(url_syncTaskSate, wmsSyncTaskStateRequest);
    }

    @Override
    public WmsGlobalResponse queryAndFrozenEnableOutbound(JobQueue jobQueue) {
        WmsOutboundResourcesRequest wmsOutboundResourcesRequest = new WmsOutboundResourcesRequest();
        wmsOutboundResourcesRequest.setTaskDetailId(jobQueue.getWmsTaskDetailId());
        wmsOutboundResourcesRequest.setLpnTypeCode(jobQueue.getWmsBoxType().getCode());
        return callApiService.postWms(url_queryAndFrozenEnableOutbound, wmsOutboundResourcesRequest);
    }

    @Override
    public WmsGlobalResponse syncTimoutMsg(List<JobTimeout> jobTimeoutList) {
        List<WmsSyncJobTimeoutRequest> wmsSyncJobTimeoutRequestList = new ArrayList<>();
        for (JobTimeout jobTimeout : jobTimeoutList) {
            WmsSyncJobTimeoutRequest wmsSyncJobTimeoutRequest = new WmsSyncJobTimeoutRequest();
            wmsSyncJobTimeoutRequest.setTaskDetailId(jobTimeout.getWmsTaskDetailId());
            wmsSyncJobTimeoutRequest.setTaskHeaderId(jobTimeout.getWmsTaskId());
            wmsSyncJobTimeoutRequest.setMsg(StringUtils.format("JOB执行超时！参数：{}", jobTimeout));

            wmsSyncJobTimeoutRequestList.add(wmsSyncJobTimeoutRequest);
        }
        return callApiService.postWms(url_broadcastNotification, wmsSyncJobTimeoutRequestList);
    }
}
