package org.nodes.wms.core.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * author: pengwei
 * date: 2021/4/19 09:27
 * description: UserTypeEnum
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

	SYSTEM(0, "系统预设用户(超级用户)"),
	CUSTOM(1, "自定义用户")
	;

	Integer index;
	String name;
}
