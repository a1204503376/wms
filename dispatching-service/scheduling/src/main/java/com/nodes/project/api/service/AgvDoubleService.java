package com.nodes.project.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nodes.project.api.domain.AgvDouble;
import com.nodes.project.api.dto.agv.AgvSyncOrderRequest;

/**
 *
 */
public interface AgvDoubleService extends IService<AgvDouble> {
    boolean existsJob(String jobId);

    void saveJob(AgvSyncOrderRequest agvSyncOrderRequest);
}
