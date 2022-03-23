package org.nodes.core.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: WmsCore
 * @description: 登录状态枚举类
 * @author: pengwei
 * @create: 2020-11-23 14:36
 **/
@Getter
@AllArgsConstructor
public enum LoginStatusEnum {

	OFF_LINE(0, "离线"),
	ON_LINE(1, "在线"),
	;

	Integer index;
	String name;
}
