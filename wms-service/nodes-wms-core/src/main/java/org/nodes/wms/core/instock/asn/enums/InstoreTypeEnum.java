package org.nodes.wms.core.instock.asn.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 入库方式枚举
 * @since 2020-11-12
 */
@Getter
@AllArgsConstructor
public enum InstoreTypeEnum {

	Normal(10, "常规入库"),
	NO_BILL(20, "无单据入库"),
	CROSS(30, "越库入库"),
	;

	Integer index;
	String name;
}
