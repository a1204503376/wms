package org.nodes.wms.core.relenishment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 补货状态
 * @author whj
 * @since 2019-12-26
 */
@Getter
@AllArgsConstructor
public enum RelStateEnum {
	UNEXECUTED("未执行", 10),
	EXECUTING("执行中", 20),
	EXECUTED("执行结束", 30);

	private String name;
	private int index;
}
