package org.nodes.wms.biz.basics.customers.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.StringPool;

/**
 * 客户表状态枚举
 **/
@Getter
@RequiredArgsConstructor
public enum StateEnum {
  //not enabled
    ENABLE(0, "启用"),
	NOTENABLE(1, "未启用"),
	;
	private final Integer index;
	private final String name;
	public static String valueOf(Integer index) {
		switch (index) {
			case 0:
				return ENABLE.getName();
			case 1:
				return NOTENABLE.getName();
			default:
				return StringPool.EMPTY;
		}
	}


}
