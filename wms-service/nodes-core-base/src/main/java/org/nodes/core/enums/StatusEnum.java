package org.nodes.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: WmsCore
 * @description: 启用状态枚举
 * @author: pengwei
 * @create: 2021-01-24 10:10
 **/
@Getter
@AllArgsConstructor
public enum StatusEnum {

	OFF(0, "禁用"),
	ON(1, "启用"),
	;

	Integer index;
	String name;
}
