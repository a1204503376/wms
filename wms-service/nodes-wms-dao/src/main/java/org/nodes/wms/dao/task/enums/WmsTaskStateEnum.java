package org.nodes.wms.dao.task.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 任务状态
 *
 * @author nodesc
 */
@Getter
@RequiredArgsConstructor
public enum WmsTaskStateEnum {
	NOT_ISSUED(1, "未下发"),
	ISSUED(2, "已下发"),
	START_EXECUTION(3, "开始执行"),
	ABNORMAL(4, "异常中断"),
	COMPLETED(5, "已完成"),
	CANCELED(6, "已取消");

	@EnumValue
	private final Integer code;

	@JsonValue
	private final String desc;
}
