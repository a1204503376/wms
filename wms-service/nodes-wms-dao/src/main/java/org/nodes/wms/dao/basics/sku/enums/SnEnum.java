package org.nodes.wms.dao.basics.sku.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否序列号物品枚举
 *
 * @Author zx
 * @Date 2020/7/27
 **/
@Getter
@AllArgsConstructor
public enum SnEnum {
	YES(1, "是"),
	NO(0, "否"),
	;


	private Integer index;
	private String name;
}
