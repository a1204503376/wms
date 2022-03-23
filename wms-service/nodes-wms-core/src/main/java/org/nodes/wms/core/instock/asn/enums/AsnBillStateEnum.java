package org.nodes.wms.core.instock.asn.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nodes.core.tool.utils.StringPool;

/**
 * @program: WmsCore
 * @description: 入库单状态枚举
 * @author: pengwei
 * @create: 2020-04-17 09:06
 **/
@Getter
@AllArgsConstructor
public enum AsnBillStateEnum {

	CREATE(10, "单据创建"),
	EXECUTING(20, "处理中"),
	PART(30, "部分收货"),
	COMPLETED(40, "已完成"),
	REPEAL(90, "已撤销"),
	CANCEL(91, "已取消"),
	;


	Integer index;
	String name;

	public static String valueOf(Integer index) {
		switch (index) {
			case 10:
				return CREATE.getName();
			case 20:
				return EXECUTING.getName();
			case 30:
				return PART.getName();
			case 40:
				return COMPLETED.getName();
			case 90:
				return REPEAL.getName();
			case 91:
				return CANCEL.getName();
			default:
				return StringPool.EMPTY;
		}
	}
}
