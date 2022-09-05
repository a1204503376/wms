package com.nodes.project.api.service;

import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.domain.JobTimeout;
import com.nodes.project.api.dto.wms.WmsGlobalResponse;

import java.util.List;

/**
 * 呼叫WMS的API接口
 */
public interface CallWmsService {

    /**
     * 调用agv异常时，将异常信息同步到WMS
     * @param jobQueue jobQueue
     * @param msg 异常信息
     */
    void syncExceptionMsgOfCallAgv(JobQueue jobQueue, String msg);

    void syncTaskState(JobQueue jobQueue, String msg);

    WmsGlobalResponse queryAndFrozenEnableOutbound(JobQueue jobQueue);

    WmsGlobalResponse syncTimoutMsg(List<JobTimeout> jobTimeoutList);
}
