package org.nodes.wms.core.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 任务类型枚举
 * @create 20200214
 */
@Getter
@AllArgsConstructor
public enum TaskTypeEnum {

	ALL("所有任务", 0),
	Check("入库清点任务", 102),
	Pick("拣货任务", 103),
	Count("盘点任务", 104),
	Putaway("上架任务", 105),
	StockPack("尾箱打包任务", 106),
	FillLpn("拼托任务", 107),
	Replenish("补货任务", 108);

	private String name;
	private int index;

}
