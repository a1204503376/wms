package org.nodes.wms.core.count.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: WmsCore
 * @description: 盘点方式枚举类
 * @author: pengwei
 * @create: 2020-12-25 11:25
 **/
@Getter
@AllArgsConstructor
public enum CountByEnum {

	SKU(0, "按物品盘点"),
	LOCATION(1, "按库位盘点"),
	;

	Integer index;
	String name;
	public static CountByEnum getCountByEnumByIndex(Integer index){
		if(SKU.getIndex().equals(index)){
			return SKU;
		}
		if(LOCATION.getIndex().equals(index)){
			return LOCATION;
		}
		return LOCATION;
	}
}
