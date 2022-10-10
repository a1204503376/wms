package com.nodes.project.api.service;

import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.dto.agv.AgvGlobalResponse;
import com.nodes.project.api.dto.agv.AgvOtherGlobalResponse;

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

    /**
     * 双重入库，重发AGV
     *
     * @param vehicle  车俩名称
     * @param location 目标库位
     */
    AgvOtherGlobalResponse vehicleWithdrawal(String vehicle, String location);
}
