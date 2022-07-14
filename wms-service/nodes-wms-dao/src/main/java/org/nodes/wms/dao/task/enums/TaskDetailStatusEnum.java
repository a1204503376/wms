package org.nodes.wms.dao.task.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 任务明细状态
 */
@Getter
@RequiredArgsConstructor
public enum TaskDetailStatusEnum {
	NORMAL(0, "正常"),
	CLOSE(1, "关闭"),
	CANCELED(2, "已取消"),
	TASK_EXCEPTION(3, "任务异常");

	@EnumValue
	private final Integer code;
	@JsonValue
	private final String desc;
}
