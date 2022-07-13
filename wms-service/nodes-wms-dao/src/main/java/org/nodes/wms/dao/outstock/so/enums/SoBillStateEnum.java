package org.nodes.wms.dao.outstock.so.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.dao.outstock.so.dto.output.SoBillStateResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum SoBillStateEnum {
	CREATE("单据创建", 10),
	EXECUTING("处理中", 20),
	PART("部分出库", 30),
	COMPLETED("已完成", 40),

	NOT("未出库",50),
	PART_STOCK_OUT("部分出库",60),
	COMPLETED_STOCK_OUT("全部出库",70),
	ALLOCATED("已分配",80),
	REPEAL("已撤销", 90),
	CANCEL("已取消",91),
	CLOSED("已关闭",92);

	@EnumValue
	final Integer index;
	@JsonValue
	final String name;

	SoBillStateEnum(String name, Integer index) {
		this.name = name;
		this.index = index;
	}

	public static String valueOf(Integer index) {
		switch(index) {
			case 10:
				return CREATE.getName();
			case 20:
				return EXECUTING.getName();
			case 30:
				return PART.getName();
			case 40:
				return COMPLETED.getName();
			case 90:
				return REPEAL.getName();
			case 91:
				return CANCEL.getName();
			default:
				return StringPool.EMPTY;
		}
	}

	public static List<SoBillStateResponse> getList() {
		List<SoBillStateResponse> soBillStateList = new ArrayList<>();
		for (SoBillStateEnum item : values()
			 ) {
			SoBillStateResponse soBillStateResponse = new SoBillStateResponse();
			soBillStateResponse.setValue(item.getIndex());
			soBillStateResponse.setLabel(item.getName());
			soBillStateList.add(soBillStateResponse);
		}
		return soBillStateList;
	}
}
