package com.nodes.project.api.listeners;

import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.dto.agv.AgvSyncOrderRequest;
import com.nodes.project.api.events.AlreadyStorageEvent;
import com.nodes.project.api.service.CallAgvService;
import com.nodes.project.api.service.JobQueueService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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

    @EventListener
    public void alreadyStorage(AlreadyStorageEvent event) {
        try {
            AgvSyncOrderRequest agvSyncOrderRequest = event.getAgvSyncOrderRequest();
            if (ObjectUtils.isEmpty(agvSyncOrderRequest)) {
                return;
            }
            JobQueue jobQueue = jobQueueService.getById(agvSyncOrderRequest.getJobId());
            //todo 调用WMSAPI 计算新的目标库位
            String location = StringUtils.EMPTY;

            callAgvService.vehicleWithdrawal(agvSyncOrderRequest.getAgvName(), location);

        } catch (Exception ex) {
            log.error("双重入库监听器执行异常：{}", ex.getMessage());
        }
    }
}
