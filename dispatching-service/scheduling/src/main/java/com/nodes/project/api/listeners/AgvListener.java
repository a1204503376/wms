package com.nodes.project.api.listeners;

import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.dto.agv.AgvSyncOrderRequest;
import com.nodes.project.api.dto.wms.WmsGlobalResponse;
import com.nodes.project.api.dto.wms.WmsResponse;
import com.nodes.project.api.events.AlreadyStorageEvent;
import com.nodes.project.api.service.CallAgvService;
import com.nodes.project.api.service.CallWmsService;
import com.nodes.project.api.service.JobQueueService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * AGV事件监听注册类
 */
@Slf4j
@Component
public class AgvListener {
    @Resource
    private JobQueueService jobQueueService;
    @Resource
    private CallAgvService callAgvService;
    @Resource
    private CallWmsService callWmsService;

    @EventListener
    public void alreadyStorage(AlreadyStorageEvent event) {
        try {
            AgvSyncOrderRequest agvSyncOrderRequest = event.getAgvSyncOrderRequest();
            if (ObjectUtils.isEmpty(agvSyncOrderRequest)) {
                log.warn("事件参数为空");
                return;
            }
            JobQueue jobQueue = jobQueueService.getById(agvSyncOrderRequest.getJobId());
            if (ObjectUtils.isEmpty(jobQueue) || ObjectUtils.isEmpty(jobQueue.getWmsTaskDetailId())) {
                log.warn("根据JobId:{}，获取对象为空", agvSyncOrderRequest.getJobId());
                return;
            }
            WmsGlobalResponse wmsGlobalResponse = callWmsService.newLocationOnDoubleWarehousing(jobQueue);
            if (wmsGlobalResponse.hasException()) {
                log.error(wmsGlobalResponse.getMsg());
                return;
            }
            WmsResponse wmsResponse = wmsGlobalResponse.getWmsResponse();
            String location = String.valueOf(wmsResponse.getData());
            log.info("重新获取的目标库位：{}", location);
            callAgvService.vehicleWithdrawal(agvSyncOrderRequest.getAgvName(), location);

        } catch (Exception ex) {
            log.error("双重入库监听器执行异常：{}", ex.getMessage());
        }
    }
}
