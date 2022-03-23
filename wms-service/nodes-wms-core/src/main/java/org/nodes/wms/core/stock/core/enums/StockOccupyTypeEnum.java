package org.nodes.wms.core.stock.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 库存占用类型
 * @create 20200217
 */
@Getter
@AllArgsConstructor
public enum StockOccupyTypeEnum {
	PickPlan("拣货分配占用", 10),
	Count("盘点占用", 20),
	ApponintmentOut("出库预约占用", 30),
	StockPack("尾箱打包占用", 10),
	Replenish("补货占用", 10);

	private String name;
	private int index;

}
