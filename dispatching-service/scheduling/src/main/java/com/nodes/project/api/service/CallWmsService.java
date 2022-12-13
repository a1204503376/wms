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
     * 调用agv时，将失败或异常信息同步到WMS
     */
    void syncExceptionMsgOfCallAgv(JobQueue jobQueue, String msg);

    /**
     * 调用agv时，将成功信息同步到WMS
     */
    void syncSuccessfulMsgOfCallAgv(JobQueue jobQueue, String msg);

    void syncTaskState(JobQueue jobQueue, String msg);

    WmsGlobalResponse queryAndFrozenEnableOutbound(JobQueue jobQueue);

    WmsGlobalResponse syncTimoutMsg(List<JobTimeout> jobTimeoutList);

    /**
     * 重新获取WMS的目标库位
     */
    WmsGlobalResponse newLocationOnDoubleWarehousing(JobQueue jobQueue);

    /**
     * 同步失败信息给WMS
     *
     * @param jobQueue 任务队列
     * @param msg 信息
     * @return true: 同步成功, false: 同步失败
     */
    boolean syncFailedMsgToWms(JobQueue jobQueue, String msg);
}
