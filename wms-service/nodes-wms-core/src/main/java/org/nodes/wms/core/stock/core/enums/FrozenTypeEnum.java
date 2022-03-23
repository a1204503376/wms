package org.nodes.wms.core.stock.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 冻结类型
 * @author whj
 * @since 2021-06-03
 */
@Getter
@AllArgsConstructor
public enum FrozenTypeEnum {
	SINGLE("单个冻结", "singleFrozen"),
	MULT("批量冻结", "multFrozen"),
	LOC("库位冻结", "locFrozen");

	private String name;
	private String value;
}
