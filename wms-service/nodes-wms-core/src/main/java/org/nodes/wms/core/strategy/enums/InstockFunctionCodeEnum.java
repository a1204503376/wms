package org.nodes.wms.core.strategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InstockFunctionCodeEnum {

	RANDOM(1, "随机存储"),
	LOCATION(2, "定址定位"),
	MERGE(3, "合并存储"),
	;

	Integer index;
	String name;
}
