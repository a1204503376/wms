package org.nodes.wms.dao.basics.warehouse.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 库位种类枚举
 **/
@Getter
@RequiredArgsConstructor
public enum LocCategoryEnum {
	/**
	 * 货架
	 */
	SHELVES(10,"货架"),

	/**
	 * 地摊
	 */
	STALL(20,"地摊"),
	;

	@EnumValue
	private final Integer code;

	@JsonValue
	private final String desc;
}
