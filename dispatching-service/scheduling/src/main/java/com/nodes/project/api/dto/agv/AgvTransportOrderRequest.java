package com.nodes.project.api.dto.agv;

import lombok.Data;

import java.util.List;

/**
 * 创建指定名称订单的请求对象
 */
@Data
public class AgvTransportOrderRequest {

    /**
     * 目标集合 固定2个
     * 第一个：起点，第二个：终点
     */
    private List<Destination> destinations;

    /**
     * 属性集合
     */
    private List<Property> properties;


}

