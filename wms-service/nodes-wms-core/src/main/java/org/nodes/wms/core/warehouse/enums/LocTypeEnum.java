package org.nodes.wms.core.warehouse.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @description 库位类型枚举
 * @since 2020-07-30
 */
@Getter
@AllArgsConstructor
public enum LocTypeEnum {

	BoxPick(10, "按箱拣货"),
	PiecePick(20, "按件(PC)拣货"),
	OnWay(30, "在途"),
	Virtual(40, "虚拟");
	private Integer index;

	private String name;
}
