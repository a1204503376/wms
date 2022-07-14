package org.nodes.wms.dao.stock.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.stock.dto.output.StockStatusResponse;

import java.util.ArrayList;
import java.util.List;

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

	public static List<StockStatusResponse> getList() {
		List<StockStatusResponse> stockStatusList = new ArrayList<>();
		for (StockStatusEnum item : values()
		) {
			StockStatusResponse stockStatusResponse = new StockStatusResponse();
			stockStatusResponse.setValue(item.getCode());
			stockStatusResponse.setLabel(item.getDesc());
			stockStatusList.add(stockStatusResponse);
		}
		return stockStatusList;
	}
}
