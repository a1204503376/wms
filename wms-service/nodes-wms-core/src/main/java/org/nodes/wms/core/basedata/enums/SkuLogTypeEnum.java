package org.nodes.wms.core.basedata.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 物品日志类型
 * @since 2020-12-09
 */
@Getter
@AllArgsConstructor
public enum SkuLogTypeEnum {

	INSTOCK(0, "入库"),
	OUTSTOCK(1, "出库"),
	;

	Integer index;
	String name;
}
