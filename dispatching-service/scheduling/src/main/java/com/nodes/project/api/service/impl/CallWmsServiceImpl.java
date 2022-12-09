package com.nodes.project.api.service.impl;

import com.nodes.common.utils.StringUtils;
import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.domain.JobTimeout;
import com.nodes.project.api.domain.SyncWtsExceptionLog;
import com.nodes.project.api.dto.wms.*;
import com.nodes.project.api.service.CallApiService;
import com.nodes.project.api.service.CallWmsService;
import com.nodes.project.api.service.SyncWtsExceptionLogService;
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

    @Resource
    private SyncWtsExceptionLogService syncWtsExceptionLogService;

    @Override
    public void syncExceptionMsgOfCallAgv(JobQueue jobQueue, String msg) {
        syncFailedMsgToWms(jobQueue, msg);
    }

    @Override
    public void syncSuccessfulMsgOfCallAgv(JobQueue jobQueue, String msg) {
        syncFailedMsgToWms(jobQueue, msg);
    }

    public boolean syncFailedMsgToWms(JobQueue jobQueue, String msg) {
        WmsSyncTaskStateRequest wmsSyncTaskStateRequest = new WmsSyncTaskStateRequest();
        wmsSyncTaskStateRequest.setTaskHeaderId(jobQueue.getWmsTaskId());
        wmsSyncTaskStateRequest.setTaskDetailId(jobQueue.getWmsTaskDetailId());
        wmsSyncTaskStateRequest.setState(jobQueue.getStatus());
        if (msg != null) {
            wmsSyncTaskStateRequest.setMsg(msg);
        }
        WmsGlobalResponse response = callApiService.postWms(url_syncTaskSate, wmsSyncTaskStateRequest);
        if (response.hasException()){
            // 同步wms任务状态发生异常时，查询异常日志中是否存在改任务，不存在则保存异常日志
            SyncWtsExceptionLog exLog = syncWtsExceptionLogService.getByTaskId(jobQueue.getWmsTaskId());
            if (exLog == null){
                syncWtsExceptionLogService.save(wmsSyncTaskStateRequest);
            }
            return false;
        }
        return true;
    }

    /**
     * 同步AGV执行状态给WMS
     */
    @Async
    @Override
    public void syncTaskState(JobQueue jobQueue, String msg) {
        syncFailedMsgToWms(jobQueue, msg);
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
