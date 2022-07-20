package org.nodes.wms.dao.instock.receive.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.enums.IPairs;

/**
 * 收货单明细接收状态枚举
 */
@Getter
@RequiredArgsConstructor
public enum DetailStatusEnum implements IPairs<Integer,String,DetailStatusEnum> {
	/**
	 *未收货
	 */
	NOT_RECEIPT(10, "未收货"),
	;

	@Override
	public DetailStatusEnum get() {
		return this;
	}
	@EnumValue
	private final Integer code;
	@JsonValue
	private final String desc;
	@Override
	public Integer key() {
		return this.code;
	}

	@Override
	public String value() {
		return this.desc;
	}
}
