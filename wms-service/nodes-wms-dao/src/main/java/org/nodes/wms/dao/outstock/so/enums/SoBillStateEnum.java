package org.nodes.wms.dao.outstock.so.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.dao.outstock.so.dto.output.SoBillStateResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 出库单状态
 *
 * @author nodesc
 */
@Getter
@AllArgsConstructor
public enum SoBillStateEnum {
	CREATE("单据创建", 10),
	EXECUTING("已分配", 20),
	PART("部分出库", 30),
	ALL_OUT_STOCK("全部出库", 35),
	COMPLETED("已关闭", 40),
	CANCELED("已取消", 90);


	@EnumValue
	final Integer code;

	@JsonValue
	final String desc;

	SoBillStateEnum(String desc, Integer code) {
		this.desc = desc;
		this.code = code;
	}

	public static String valueOf(Integer index) {
		switch (index) {
			case 10:
				return CREATE.getDesc();
			case 20:
				return EXECUTING.getDesc();
			case 30:
				return PART.getDesc();
			case 35:
				return ALL_OUT_STOCK.getDesc();
			case 40:
				return COMPLETED.getDesc();
			case 90:
				return CANCELED.getDesc();
			default:
				return StringPool.EMPTY;
		}
	}

	public static List<SoBillStateResponse> getList() {
		List<SoBillStateResponse> soBillStateList = new ArrayList<>();
		for (SoBillStateEnum item : values()) {
			SoBillStateResponse soBillStateResponse = new SoBillStateResponse();
			soBillStateResponse.setValue(item.getCode());
			soBillStateResponse.setLabel(item.getDesc());
			soBillStateList.add(soBillStateResponse);
		}
		return soBillStateList;
	}
}
