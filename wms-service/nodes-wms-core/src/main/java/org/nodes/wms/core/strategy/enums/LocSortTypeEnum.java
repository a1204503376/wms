package org.nodes.wms.core.strategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: WmsCore
 * @description: 库位排序类型枚举类
 * @author: pengwei
 * @create: 2020-11-18 14:05
 **/
@Getter
@AllArgsConstructor
public enum LocSortTypeEnum {

	LOC_CODE(0, "库位编码"),
	LOC_NAME(1, "库位名称"),
	;

	Integer index;
	String name;
}
