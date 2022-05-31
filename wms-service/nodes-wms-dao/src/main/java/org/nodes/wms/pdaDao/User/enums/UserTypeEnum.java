package org.nodes.wms.pdaDao.User.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {

	SYSTEM(0, "系统预设用户(超级用户)"),
	CUSTOM(1, "自定义用户")
	;

	Integer index;
	String name;
}
