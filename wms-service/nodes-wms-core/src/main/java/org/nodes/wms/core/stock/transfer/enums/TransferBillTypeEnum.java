package org.nodes.wms.core.stock.transfer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 库内移动单据类型
 * @since 2020-08-03
 */
@Getter
@AllArgsConstructor
public enum TransferBillTypeEnum {

	Move("移库", 10),
	Replenish("补货", 20),
	Putaway("上架",30),
	QualityControl("质检",40),
	MoveVirtual("虚拟移库单", 50);

	private String name;

	private Integer index;
}
