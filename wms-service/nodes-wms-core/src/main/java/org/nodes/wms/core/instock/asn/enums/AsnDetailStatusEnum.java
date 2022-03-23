package org.nodes.wms.core.instock.asn.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 入库单明细状态枚举
 * @since 2020-11-09
 */
@Getter
@AllArgsConstructor
public enum AsnDetailStatusEnum {

	UNRECEIVED(10, "未接收"),
	RECEIVED(20, "已接收"),
	;

	Integer index;
	String name;
}
