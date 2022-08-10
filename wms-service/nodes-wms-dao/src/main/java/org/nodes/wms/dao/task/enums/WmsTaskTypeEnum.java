package org.nodes.wms.dao.task.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nodes.core.enums.BaseEnum;
import org.nodes.wms.dao.task.dto.output.TaskTypeSelectResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务类别
 *
 * @author nodesc
 */
@Getter
@RequiredArgsConstructor
public enum WmsTaskTypeEnum implements BaseEnum {
	PUTAWAY(1, "上架"),
	PICKING(2, "拣货"),
	STOCK_COUNT(3, "盘点"),
	AGV_PUTAWAY(101, "AGV上架"),
	AGV_PICKING(102, "AGV拣货"),
	AGV_STOCK_MOVE(303, "AGV库内移位");

	@EnumValue
	private final Integer code;

	@JsonValue
	private final String desc;

	public static List<TaskTypeSelectResponse> getList() {
		List<TaskTypeSelectResponse> list = new ArrayList<>();
		for (WmsTaskTypeEnum item : values()) {
			TaskTypeSelectResponse taskTypeSelectResponse = new TaskTypeSelectResponse();
			taskTypeSelectResponse.setLabel(item.desc);
			taskTypeSelectResponse.setValue(item.code);
			list.add(taskTypeSelectResponse);
		}
		return list;
	}
}
