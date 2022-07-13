package com.nodes.project.api.dto.agv;

import lombok.Data;

/**
 * 撤回指定名称的订单 请求对象
 */
@Data
public class AgvWithdrawalRequest {

    /**
     * 订单名称
     * JOB ID
     */
    private String name;
}
