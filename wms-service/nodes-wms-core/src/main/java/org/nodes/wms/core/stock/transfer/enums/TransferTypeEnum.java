package org.nodes.wms.core.stock.transfer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: WmsCore
 * @description: 移动类型枚举类
 * @author: pengwei
 * @create: 2020-12-22 16:30
 **/
@Getter
@AllArgsConstructor
public enum TransferTypeEnum {

	PUTAWAY(10, "上架"),
	QUALITY_TEST(20, "质检"),
	REPLENISH(30, "补货"),
	INNER_WAREHOUSE(40, "库内"),
	;
	Integer index;
	String name;
}
