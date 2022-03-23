package org.nodes.wms.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 创建类型枚举
 * @since 2020-11-11
 */
@Getter
@AllArgsConstructor
public enum CreateTypeEnum {

	INNER(10, "内部创建"),
	OUTER(20, "外部创建")
	;

	Integer index;
	String name;
}
