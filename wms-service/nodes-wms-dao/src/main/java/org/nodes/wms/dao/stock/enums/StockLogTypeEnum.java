package org.nodes.wms.dao.stock.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.stock.dto.output.StockLogTypeResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 库存来源类型（库存日志类型）
 */
@Getter
@RequiredArgsConstructor
public enum StockLogTypeEnum {

	INSTOCK_BY_PCS(101, "按件收货-手持"),
	INSTOCK_BY_BOX(102, "按箱收货-手持"),
	INSTOCK_BY_MULTI_BOX(103, "多箱收货-手持"),
	INSTOCK_BY_CANCEL_PICK(104, "撤销拣货"),
	INSTOCK_BY_PUTAWAY(105, "按箱上架"),
	INSTOCK_BY_Import(106, "导入库存"),
	INSTOCK_BY_PC(107, "PC收货"),
	STOCK_FREEZE(201, "库存冻结"),
	STOCK_UNFREEZE(202, "库存解冻"),
	STOCK_MOVE_BY_START_INSTOCK_TASK(203, "入库任务开始执行"),
	STOCK_MOVE_BY_END_INSTOCK_TASK(204, "入库任务成功执行"),
	STOCK_MOVE_BY_START_OUTSTOCK_TASK(205, "出库任务开始执行"),
	STOCK_MOVE_BY_END_OUTSTOCK_TASK(206, "出库任务成功执行"),
	STOCK_TO_INSTOCK_RECE(207, "到入库接驳区"),
	STOCK_MOVE_BY_PCS(208, "PC按件移位"),
	STOCK_MOVE_BY_BOX(209, "PC按箱移位"),

	OUTSTOCK_BY_PICK(301, "拣货出库"),
	OUTSTOCK_BY_CANCEL_RECEIVE(302, "撤销收货");

	private final Integer code;
	@EnumValue
	@JsonValue
	private final String desc;

	public static List<StockLogTypeResponse> getList() {
		List<StockLogTypeResponse> list = new ArrayList<>();
		for (StockLogTypeEnum item : values()) {
			StockLogTypeResponse stockLogTypeResponse = new StockLogTypeResponse();
			stockLogTypeResponse.setLabel(item.desc);
			stockLogTypeResponse.setValue(item.code);
			list.add(stockLogTypeResponse);
		}
		return list;
	}
}
