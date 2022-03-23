package org.nodes.wms.core.warehouse.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 库区类型枚举
 * @since 2020-11-12
 */
@Getter
@AllArgsConstructor
public enum ZoneTypeEnum {

	NON_CONFORMITY(50, "不合格品区"),
	VIRTUAL(70, "虚拟库区"),
	BACKUP(71, "备货区"),
	TILTING(72, "翻包区"),
	SHIPPING_INSTOCK(73, "入库集货区"),
	SHIPPING_OUTSTOCK(74, "出库集货区"),
	PICK(75, "拣货区"),
	QC(76, "质检区"),
	STORAGE(77, "存储区"),
	;

	Integer index;
	String name;
}
