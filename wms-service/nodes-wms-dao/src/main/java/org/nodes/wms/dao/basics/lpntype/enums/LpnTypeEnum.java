package org.nodes.wms.dao.basics.lpntype.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.enums.IPairs;

/**
 * LpnTyp类型
 **/
@Getter
@RequiredArgsConstructor
public enum LpnTypeEnum
	implements IPairs<Integer,String, LpnTypeEnum> {
	/**
	 * 1表示类型为托
	 */
	LPN(1, "托"),
	/**
	 * 2表示类型为外箱
	 */
	BOX(2, "外箱"),
	/**
	 * 3表示类型为内箱
	 */
	INNER_BOX(3, "内箱"),
	;


	@EnumValue
	private final Integer code;
	@JsonValue
	private final String desc;

	@Override
	public LpnTypeEnum get() {
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

	@Override
	public String toString() {
		return this.desc;
	}
}
