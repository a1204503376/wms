package org.nodes.wms.dao.count.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.enums.IPairs;

/**
 * 盘点单明细状态
 */
@Getter
@RequiredArgsConstructor
public enum CountDetailStateEnum
	implements IPairs<Integer,String, CountDetailStateEnum> {

	/**
	 * 未盘点
	 */
	NOT_COUNTED(1, "未盘点"),

	/**
	 * 已盘点
	 */
	COUNTED(2, "已盘点"),
	;

	@EnumValue
	@JsonValue
	private final Integer code;
	private final String desc;

	@Override
	public CountDetailStateEnum get() {
		return this;
	}

	@Override
	public Integer key() {
		return this.code;
	}

	@Override
	public String value() {
		return this.desc;
	}
}
