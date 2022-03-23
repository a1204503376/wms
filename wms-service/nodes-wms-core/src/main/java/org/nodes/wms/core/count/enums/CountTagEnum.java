package org.nodes.wms.core.count.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: WmsCore
 * @description: 盘点标签枚举
 * @author: pengwei
 * @create: 2020-12-17 13:17
 **/
@Getter
@AllArgsConstructor
public enum CountTagEnum {

	DAY(10, "日盘"),
	MONTH(20, "月盘"),
	OTHER(30, "其他"),
	;

	Integer index;
	String name;
}
