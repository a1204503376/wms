package org.nodes.core.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: WmsCore
 * @description: 菜单类型枚举
 * @author: pengwei
 * @create: 2020-11-23 10:25
 **/
@Getter
@AllArgsConstructor
public enum MenuCategoryEnum {
	MENU(1, "菜单"),
	BUTTON(2, "按钮"),
	;

	Integer index;
	String name;
}
