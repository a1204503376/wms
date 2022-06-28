package org.nodes.wms.dao.stock.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 库存状态
 */
@Getter
@RequiredArgsConstructor
public enum StockStatusEnum {
	NORMAL(0, "正常"),
	FREEZE(1, "冻结");

	@EnumValue
	private final Integer code;
	@JsonValue
	private final String desc;
}
