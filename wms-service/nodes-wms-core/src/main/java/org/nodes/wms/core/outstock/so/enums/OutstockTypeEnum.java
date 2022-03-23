package org.nodes.wms.core.outstock.so.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 出库方式枚举
 * @since 2020-11-12
 */
@Getter
@AllArgsConstructor
public enum OutstockTypeEnum {

	Normal(40, "常规出库"),
	;

	Integer index;
	String name;
}
