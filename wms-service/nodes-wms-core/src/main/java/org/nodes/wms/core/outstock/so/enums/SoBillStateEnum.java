package org.nodes.wms.core.outstock.so.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nodes.core.tool.utils.StringPool;

/**
 * @author pengwei
 * @program WmsCore
 * @description 出库单单据状态枚举
 * @create 20200224
 */
@Getter
@AllArgsConstructor
public enum SoBillStateEnum {
	CREATE("单据创建", 10),
	EXECUTING("处理中", 20),
	PART("部分出库", 30),
	COMPLETED("已完成", 40),
	REPEAL("已撤销", 90),
	CANCEL("已取消",91);

	Integer index;
	String name;

	private SoBillStateEnum(String name, Integer index) {
		this.name = name;
		this.index = index;
	}

	public static String valueOf(Integer index) {
		switch(index) {
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
