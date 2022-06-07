package org.nodes.wms.dao.basics.warehouse.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 库位处理枚举
 **/
@Getter
@RequiredArgsConstructor
public enum LocHandLingEnum {

	/**
	 * 托盘
	 */
	TRAY(10,"托盘"),

	/**
	 * 箱
	 */
	BOX(20,"箱"),

	/**
	 * 其他
	 */
	OTHER(30,"其他")
	;

	@EnumValue
	private final Integer code;

	@JsonValue
	private final String desc;
}
