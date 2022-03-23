package org.nodes.core.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: WmsCore
 * @description: 显隐枚举类
 * @author: pengwei
 * @create: 2020-11-23 10:28
 **/
@Getter
@AllArgsConstructor
public enum VisibleEnum {

	SHOW(0, "显示"),
	HIDE(1, "隐藏"),
	;

	Integer index;
	String name;
}
