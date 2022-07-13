package com.nodes.project.api.service;

import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.dto.agv.AgvGlobalResponse;

/**
 * 呼叫AGV的API接口
 */
public interface CallAgvService {

    /**
     * 创建指定名称的订单
     */
    AgvGlobalResponse transportOrders(JobQueue jobQueue);

    /**
     * 撤回指定名称的订单
     */
    AgvGlobalResponse withdrawal(JobQueue jobQueue);
}
