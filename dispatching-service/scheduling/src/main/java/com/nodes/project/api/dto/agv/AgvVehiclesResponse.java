package com.nodes.project.api.dto.agv;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 针对AGV状态返回对象
 */
@Data
public class AgvVehiclesResponse {

    private String name;

    private Integer energyLevel;

    private String integrationLevel;

    private String procState;

    private String transportOrder;

    private String currentPosition;

    private String state;

    private Integer x;

    private Integer y;

    private String type;

    private BigDecimal angle;

    private String id;

    private String modelNumber;
}
