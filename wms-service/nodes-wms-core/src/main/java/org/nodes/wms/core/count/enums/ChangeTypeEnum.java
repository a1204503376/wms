package org.nodes.wms.core.count.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: WmsCore
 * @description: 变动类型枚举类
 * @author: pengwei
 * @create: 2020-12-22 15:37
 **/
@Getter
@AllArgsConstructor
public enum ChangeTypeEnum {

	INSTOCK(0, "入库"),
	OUTSTOCK(1, "出库"),
	MOVE(2, "移动"),
	;

	Integer index;
	String name;
}
