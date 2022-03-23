package org.nodes.wms.core.outstock.so.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: purefamily_wms_java
 * @description: 分配类型
 * @author: pengwei
 * @create: 2021-07-21 18:15
 **/
@Getter
@AllArgsConstructor
public enum CreatePickPlanByWellenTypeEnum {

	FIFO(0, "先进先出"),
	LIFO(1, "后进先出"),
	LOT_RANGE(2, "批次范围"),
	ZONE_PRIORITY(3, "库区优先"),
	LOT(4, "指定批次"),
	LOCATION(5, "指定库位"),
	LOCATION_LOT(6, "指定库位&批次")
	;

	Integer index;
	String name;
}
