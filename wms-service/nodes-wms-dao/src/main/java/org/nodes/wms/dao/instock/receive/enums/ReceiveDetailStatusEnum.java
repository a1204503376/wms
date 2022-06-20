package org.nodes.wms.dao.instock.receive.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 收货明细状态
 */
@Getter
@RequiredArgsConstructor
public enum ReceiveDetailStatusEnum {
	/**
	 *未收货
	 */
	NOT_RECEIPT(10, "未收货"),

	/**
	 * 部分收货
	 */
	PART(20,"部分收货"),

	/**
	 * 全部收货
	 */
	COMPLETED(30, "全部收货"),

	/**
	 * 已取消
	 */
	CANCEL(90, "已取消"),
	;

	@EnumValue
	private final Integer code;
	@JsonValue
	private final String desc;
}
