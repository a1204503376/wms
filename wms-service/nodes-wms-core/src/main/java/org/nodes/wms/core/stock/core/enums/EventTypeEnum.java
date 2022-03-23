package org.nodes.wms.core.stock.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事务类型
 * @author pengwei
 * @since 2019-12-26
 */
@Getter
@AllArgsConstructor
public enum EventTypeEnum {
	OutStock("出库", 1),
	Count("盘点", 2),
	Check("清点", 3),
	Move("移库", 4),
	Putaway("上架",5),
	LoadingStock("装车",6),
	InstockCancel("入库取消", 8),
	;

	private String name;
	private int index;
}
