package org.nodes.wms.core.strategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: WmsCore
 * @description: 混放模式枚举类
 * @author: pengwei
 * @create: 2020-11-18 14:46
 **/
@Getter
@AllArgsConstructor
public enum MixModeEnum {

	UN_ALLOW(1, "不允许"),
	ALLOW(2, "允许"),
	;

	Integer index;
	String name;
}
