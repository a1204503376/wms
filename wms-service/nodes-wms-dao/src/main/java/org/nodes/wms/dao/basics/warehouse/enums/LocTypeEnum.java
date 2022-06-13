package org.nodes.wms.dao.basics.warehouse.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nodes.core.tool.enums.IPairs;

/**
 * 库位类型枚举
 */
@Getter
@AllArgsConstructor
public enum LocTypeEnum implements IPairs<Integer,String, LocTypeEnum> {

	BoxPick(10, "按箱拣货"),
	PiecePick(20, "按件(PC)拣货"),
	OnWay(30, "在途"),
	Virtual(40, "虚拟");

	@EnumValue
	private final Integer code;

	@JsonValue
	private final String desc;

	@Override
	public LocTypeEnum get() {
		return this;
	}

	@Override
	public Integer key() {
		return this.code;
	}

	@Override
	public String value() {
		return this.desc;
	}
}
