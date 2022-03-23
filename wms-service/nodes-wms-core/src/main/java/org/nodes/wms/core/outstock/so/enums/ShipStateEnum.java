package org.nodes.wms.core.outstock.so.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wuhj
 * @program WmsCore
 * @description 发运状态枚举
 * @since 2020-11-26
 */
@Getter
@AllArgsConstructor
public enum ShipStateEnum {

	DEFAULT(10, "未发运"),
	SENDING(20, "发运中"),
	SENDED(30, "已发运")
	;

	Integer index;
	String name;
}
