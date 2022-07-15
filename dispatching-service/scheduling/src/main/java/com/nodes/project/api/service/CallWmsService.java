package com.nodes.project.api.service;

import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.domain.JobTimeout;
import com.nodes.project.api.dto.wms.WmsGlobalResponse;

import java.util.List;

/**
 * 呼叫WMS的API接口
 */
public interface CallWmsService {

    void syncTaskState(JobQueue jobQueue);

    WmsGlobalResponse queryAndFrozenEnableOutbound(JobQueue jobQueue);

    WmsGlobalResponse syncTimoutMsg(List<JobTimeout> jobTimeoutList);
}
