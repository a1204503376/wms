package org.nodes.wms.dao.task.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 任务执行方式
 */
@Getter
@RequiredArgsConstructor
@Deprecated
public enum ProcTypeEnum {
	RUNNING(1, "执行中"),
	PICKING(0, "不发布任务");

	@EnumValue
	private final Integer code;
	@JsonValue
	private final String desc;
}
