package org.nodes.wms.core.outstock.so.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springblade.core.tool.utils.StringPool;

/**
 * @author pengwei
 * @program WmsCore
 * @description 波次状态枚举
 * @create 20200210
 */
@Getter
@AllArgsConstructor
public enum WellenStateEnum {
	Create("波次创建", 10),
	AllocComplated("分配完成", 20),
	Begin("开始执行", 30),
	Close("波次关闭", 40);

	private String name;
	private int index;

	public static String getName(int index) {
		for (WellenStateEnum wellenState : WellenStateEnum.values()) {
			if (wellenState.getIndex() == index) {
				return wellenState.getName();
			}
		}
		return StringPool.EMPTY;
	}
}
