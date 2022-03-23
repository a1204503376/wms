package org.nodes.wms.core.basedata.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 订单类型处理类型枚举
 * @since 2020-11-16
 */
@Getter
@AllArgsConstructor
public enum BillTypeActionTypeEnum {

	BASE(1, "按件"),
	LPN(2, "按托"),
	LOT(3, "按批"),
	OTHER(4, "其他");

	Integer index;
	String name;
}
