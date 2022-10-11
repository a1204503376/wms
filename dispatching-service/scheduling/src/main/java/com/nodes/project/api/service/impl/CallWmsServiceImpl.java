package com.nodes.project.api.service.impl;

import com.nodes.common.utils.StringUtils;
import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.domain.JobTimeout;
import com.nodes.project.api.dto.wms.*;
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
    private static final String URL_NEWLOCATIONONDOUBLEWAREHOUSING = "/newLocationOnDoubleWarehousing";


    @Resource
    private CallApiService callApiService;

    @Override
    public void syncExceptionMsgOfCallAgv(JobQueue jobQueue, String msg) {
        WmsSyncTaskStateRequest wmsSyncTaskStateRequest = new WmsSyncTaskStateRequest();
        wmsSyncTaskStateRequest.setTaskHeaderId(jobQueue.getWmsTaskId());
        wmsSyncTaskStateRequest.setTaskDetailId(jobQueue.getWmsTaskDetailId());
        if (msg != null) {
            wmsSyncTaskStateRequest.setMsg(msg);
        }
        callApiService.postWms(url_syncTaskSate, wmsSyncTaskStateRequest);
    }

    /**
     * 同步AGV执行状态给WMS
     */
    @Async
    @Override
    public void syncTaskState(JobQueue jobQueue, String msg) {
        WmsSyncTaskStateRequest wmsSyncTaskStateRequest = new WmsSyncTaskStateRequest();
        wmsSyncTaskStateRequest.setTaskHeaderId(jobQueue.getWmsTaskId());
        wmsSyncTaskStateRequest.setTaskDetailId(jobQueue.getWmsTaskDetailId());
        wmsSyncTaskStateRequest.setState(jobQueue.getStatus());
        if (msg != null) {
            wmsSyncTaskStateRequest.setMsg(msg);
        }
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

    @Override
    public WmsGlobalResponse newLocationOnDoubleWarehousing(JobQueue jobQueue) {
        NewLocationOnDoubleWarehousingRequest newLocationOnDoubleWarehousingRequest = new NewLocationOnDoubleWarehousingRequest();
        newLocationOnDoubleWarehousingRequest.setTaskId(String.valueOf(jobQueue.getWmsTaskDetailId()));
        return callApiService.postWms(URL_NEWLOCATIONONDOUBLEWAREHOUSING, newLocationOnDoubleWarehousingRequest);
    }
}
