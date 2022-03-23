package org.nodes.wms.core.stock.transfer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 库内移动单据状态
 * @since 2020-08-03
 */
@Getter
@AllArgsConstructor
public enum TransferBillStateEnum {

	Create("单据创建", 10),
	Executing("正在执行", 20),
	Complated("已完成", 30),
	Cancel("已取消", 40);

	private String name;
	private Integer index;
}
