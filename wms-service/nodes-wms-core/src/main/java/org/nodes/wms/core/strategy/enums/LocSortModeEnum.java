package org.nodes.wms.core.strategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: WmsCore
 * @description: 库位排序方式枚举类
 * @author: pengwei
 * @create: 2020-11-18 14:10
 **/
@Getter
@AllArgsConstructor
public enum LocSortModeEnum {

	ASC(0, "升序"),
	DESC(1, "降序"),
	;

	Integer index;
	String name;
}
