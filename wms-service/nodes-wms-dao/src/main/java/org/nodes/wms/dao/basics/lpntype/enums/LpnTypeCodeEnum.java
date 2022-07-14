package org.nodes.wms.dao.basics.lpntype.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 天宜定制功能
 */
@Getter
@RequiredArgsConstructor
public enum LpnTypeCodeEnum {
	A("A", "A箱"),

	B("B", "B箱"),

	C("C", "C箱"),

	D("D", "D箱"),
	UNKNOWN("0", "未知箱型");

	@EnumValue
	private final String code;
	@JsonValue
	private final String desc;
}
