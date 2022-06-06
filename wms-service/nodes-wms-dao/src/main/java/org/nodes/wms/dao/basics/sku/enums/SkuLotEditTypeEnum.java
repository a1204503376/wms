package org.nodes.wms.dao.basics.sku.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: WmsCore
 * @description:批属性批掩码生成规则
 * @author: pengwei
 * @create: 2020-12-18 15:37
 **/
@Getter
@AllArgsConstructor
public enum SkuLotEditTypeEnum {
	DEFAULT(0,"默认"),
	AUTO_EDIT(1, "自动允许用户编辑"),
	AUTO_UN_EDIT(2, "自动补允许用户编辑"),
	USER_INPUT(3, "手动用户请求"),
	;

	Integer index;
	String name;
}
