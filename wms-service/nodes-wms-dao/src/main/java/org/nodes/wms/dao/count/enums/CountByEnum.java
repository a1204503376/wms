package org.nodes.wms.dao.count.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.enums.IPairs;

/**
 * 盘点方式枚举类
 **/
@Getter
@RequiredArgsConstructor
public enum CountByEnum
	implements IPairs<Integer,String, CountByEnum> {

	/**
	 * 按物品盘点
	 */
	SKU(1, "按物品盘点"),

	/**
	 * 按库位盘点
	 */
	LOCATION(2, "按库位盘点"),
	;

	@EnumValue
	@JsonValue
	private final Integer code;
	private final String desc;

	@Override
	public CountByEnum get() {
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
