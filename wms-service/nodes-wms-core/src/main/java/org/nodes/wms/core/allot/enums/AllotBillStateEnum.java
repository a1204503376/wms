package org.nodes.wms.core.allot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nodes.core.tool.utils.StringPool;
import org.springblade.core.tool.utils.Func;

import java.util.Arrays;

/**
 * @program: WmsCore
 * @description: 调拨单状态枚举类
 * @author: pengwei
 * @create: 2020-12-03 18:18
 **/
@Getter
@AllArgsConstructor
public enum AllotBillStateEnum {

	UN_OUTSTOCK(10, "待出库"),
	OUTSTOCKING(20, "出库处理中"),
	UN_INSTOCK(30, "待入库"),
	INSTOCKING(40, "入库处理中"),
	COMPLETED(50, "已完成"),
	CANCEL(60, "已取消");

	Integer index;
	String name;

	public static String valueOf(Integer index) {
		AllotBillStateEnum allotBillStateEnum =  Arrays.stream(AllotBillStateEnum.values()).filter(u->{
			return u.getIndex().equals(index);
		}).findFirst().orElse(null);
		return Func.isEmpty(allotBillStateEnum) ? StringPool.EMPTY : allotBillStateEnum.name;
	}
}
