package com.nodes.project.api.listeners;

import com.nodes.common.utils.StringUtils;
import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.events.AlreadyStorageEvent;
import com.nodes.project.api.service.JobQueueService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * AGV事件监听注册类
 */
@Component
public class AgvListener {
    @Resource
    private JobQueueService jobQueueService;

    @EventListener
    public void alreadyStorage(AlreadyStorageEvent event) {
        try {
            String jobId = event.getJobId();
            if (StringUtils.isEmpty(jobId)) {
                return;
            }
            JobQueue jobQueue = jobQueueService.getById(jobId);
            // 调用WMSAPI 计算新的目标库位
            // 组装参数 调用AGV新增接口:vehicleWithdrawal
        } catch (Exception ex) {

        }
    }
}
