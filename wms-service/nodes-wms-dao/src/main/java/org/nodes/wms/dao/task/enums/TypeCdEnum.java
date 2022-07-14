package org.nodes.wms.dao.task.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 任务类别
 */
@Getter
@RequiredArgsConstructor
public enum TypeCdEnum {

	PUTAWAY(1, "上架"),
	PICKING(2, "拣货"),
	STOCK_COUNT(3, "盘点");

	@EnumValue
	private final Integer code;
	@JsonValue
	private final String desc;
}
