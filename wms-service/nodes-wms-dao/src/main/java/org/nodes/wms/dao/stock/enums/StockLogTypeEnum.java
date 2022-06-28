package org.nodes.wms.dao.stock.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 库存来源类型（库存日志类型）
 */
@Getter
@RequiredArgsConstructor
public enum StockLogTypeEnum {

	INSTOCK_BY_PCS(101, "按件收货-手持"),
	INSTOCK_BY_BOX(102, "按箱收货-手持"),
	INSTOCK_BY_MULTI_BOX(103, "多箱收货-手持"),

	STOCK_FREEZE(201, "库存冻结"),
	STOCK_UNFREEZE(202, "库存解冻"),
	STOCK_MOVE_BY_START_INSTOCK_TASK(203, "入库任务开始执行"),
	STOCK_MOVE_BY_END_INSTOCK_TASK(204, "入库任务成功执行"),
	STOCK_MOVE_BY_START_OUTSTOCK_TASK(205, "出库任务开始执行"),
	STOCK_MOVE_BY_END_OUTSTOCK_TASK(206, "出库任务成功执行"),

	OUTSTOCK_BY_PICK(301, "拣货出库");

	@EnumValue
	private final Integer code;
	@JsonValue
	private final String desc;
}
