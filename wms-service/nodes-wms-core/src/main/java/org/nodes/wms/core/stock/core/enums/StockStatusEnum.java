package org.nodes.wms.core.stock.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 库存状态枚举
 *
 * @author pengwei
 * @since 2019-12-26
 */
@Getter
@AllArgsConstructor
public enum StockStatusEnum {
	NORMAL("正常", 0),
	LOCK_FILL("完全锁定", 1),
	DISCARD("废弃", 2),
	LOCK_BLOCK("部分锁定", 3);

	private String name;
	private Integer index;
}
