package org.nodes.wms.core.count.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: WmsCore
 * @description: 盘点方式枚举类
 * @author: pengwei
 * @create: 2020-12-22 14:53
 **/
@Getter
@AllArgsConstructor
public enum CountTypeEnum {

	ABC(0, "ABC盘点"),
	CHANGE(1, "动碰盘点"),
	POINT_TO(2, "指定盘点")
	;

	Integer index;
	String name;
}
