package org.nodes.wms.dao.lendreturn.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.enums.IPairs;

/**
 * 借出归还类别
 */
@Getter
@RequiredArgsConstructor
public enum LendReturnTypeEnum
	implements IPairs<Integer,String, LendReturnTypeEnum> {

	/**
	 * 借出
	 */
	LEND(1, "借出"),

	/**
	 * 归还
	 */
	RETURN(2,"归还"),

	;

	@EnumValue
	@JsonValue
	private final Integer code;
	private final String desc;

	@Override
	public LendReturnTypeEnum get() {
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
