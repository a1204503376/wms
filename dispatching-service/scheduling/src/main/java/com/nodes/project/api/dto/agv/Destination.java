package com.nodes.project.api.dto.agv;

import lombok.Data;

/**
 * AGV创建订单目标
 */
@Data
public class Destination {
    /**
     * 位置(起点或者终点)
     * WMS传库位编码，由AGV系统维护库位编码和具体的物理点位关系
     */
    private String locationName;
    /**
     * 取货动作：层数 可为空
     */
    private String operation;
}
