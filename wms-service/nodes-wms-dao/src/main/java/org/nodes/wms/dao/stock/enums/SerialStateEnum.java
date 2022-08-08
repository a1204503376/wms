package org.nodes.wms.dao.stock.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nodes.core.enums.BaseEnum;
import org.nodes.wms.dao.stock.dto.output.SerialStateResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 序列号状态
 */
@Getter
@RequiredArgsConstructor
public enum SerialStateEnum implements BaseEnum {

	IN_STOCK(0, "在库"),
	OUT_STOCK(1, "已出库");

	@EnumValue
	private final Integer code;
	@JsonValue
	private final String desc;

	public static List<SerialStateResponse> getList(){
		List<SerialStateResponse> serialStateList = new ArrayList<>();
		for (SerialStateEnum stateEnum : SerialStateEnum.values()) {
			SerialStateResponse serial = new SerialStateResponse();
			serial.setLabel(stateEnum.desc);
			serial.setValue(stateEnum.code);
			serialStateList.add(serial);
		}
		return serialStateList;
	}
}
