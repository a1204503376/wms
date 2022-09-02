package org.nodes.wms.dao.task.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nodes.core.enums.BaseEnum;
import org.nodes.wms.dao.task.dto.output.TaskStateSelectResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务状态
 *
 * @author nodesc
 */
@Getter
@RequiredArgsConstructor
public enum WmsTaskStateEnum implements BaseEnum {
	NOT_ISSUED(1, "未下发"),
	ISSUED(2, "已下发"),
	START_EXECUTION(3, "开始执行"),
	ABNORMAL(4, "异常中断中"),
	AGV_COMPLETED(5, "AGV完成"),
	COMPLETED(6, "已完成"),
	CANCELED(7, "已取消");

	@EnumValue
	private final Integer code;

	@JsonValue
	private final String desc;

	public static List<TaskStateSelectResponse> getList() {
		List<TaskStateSelectResponse> list = new ArrayList<>();
		for (WmsTaskStateEnum item : values()) {
			TaskStateSelectResponse taskStateSelectResponse = new TaskStateSelectResponse();
			taskStateSelectResponse.setLabel(item.desc);
			taskStateSelectResponse.setValue(item.code);
			list.add(taskStateSelectResponse);
		}
		return list;
	}
}
