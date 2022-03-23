package org.nodes.wms.core.stock.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 库存处理类型
 * @create 20200214
 */
@Getter
@AllArgsConstructor
public enum StockProcTypeEnum {
	New("新增", 0),
	Update("更新", 1),
	Delete("删除", 2);

	private String name;
	private int index;
}
