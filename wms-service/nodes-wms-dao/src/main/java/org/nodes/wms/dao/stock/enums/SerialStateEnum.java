package org.nodes.wms.dao.stock.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 序列号状态
 */
@Getter
@RequiredArgsConstructor
public enum SerialStateEnum {

	IN_STOCK(0, "在库"),
	OUT_STOCK(1, "已出库");

	@EnumValue
	private final Integer code;
	@JsonValue
	private final String desc;
}
