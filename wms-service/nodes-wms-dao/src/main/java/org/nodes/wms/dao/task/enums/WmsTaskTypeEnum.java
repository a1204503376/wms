package org.nodes.wms.dao.task.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 任务类别
 *
 * @author nodesc
 */
@Getter
@RequiredArgsConstructor
public enum WmsTaskTypeEnum {

	PUTAWAY(1, "上架"),
	PICKING(2, "拣货"),
	STOCK_COUNT(3, "盘点"),
	AGV_PUTAWAY(10, "AGV上架"),
	AGV_PICKING(20, "AGV拣货"),
	AGV_STOCK_MOVE(30, "AGV库内移位");

	@EnumValue
	private final Integer code;

	@JsonValue
	private final String desc;
}
