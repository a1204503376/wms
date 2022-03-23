package org.nodes.wms.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认标识枚举
 */
@Getter
@AllArgsConstructor
public enum DefaultFlagEnum {
	TRUE(1, "是"),
	FALSE(0, "否");


	private int index;
	private String name;
}
