package org.nodes.wms.dao.count.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 盘点单类型
 * @since 2020-11-09
 */
@Getter
@AllArgsConstructor
public enum StockCountTypeEnum {

	DAY(10, "日盘"),
	MONTH(20, "月盘"),
	OTHER(30, "其他"),
	RANDOM(40, "随机"),
	;


	private Integer index;
	private String name;
}
