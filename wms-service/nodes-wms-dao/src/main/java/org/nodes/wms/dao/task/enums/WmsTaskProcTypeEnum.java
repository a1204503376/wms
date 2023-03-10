package org.nodes.wms.dao.task.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 任务执行方式
 *
 * @author nodesc
 */
@Getter
@RequiredArgsConstructor
public enum WmsTaskProcTypeEnum {
	BY_PCS(1, "按件执行"),
	BY_BOX(2, "按箱执行"),
	BY_LPN(3, "按LPN执行"),
	BY_LOC(4, "按库位执行")
	;

	@EnumValue
	private final Integer code;

	@JsonValue
	private final String desc;
}
