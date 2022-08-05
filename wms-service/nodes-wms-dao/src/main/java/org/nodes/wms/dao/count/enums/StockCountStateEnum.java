package org.nodes.wms.dao.count.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.enums.IPairs;

/**
 * 盘点单状态
 */
@Getter
@RequiredArgsConstructor
public enum StockCountStateEnum
	implements IPairs<Integer,String, StockCountStateEnum> {

	/**
	 * 创建
	 */
	CREATE(1, "创建"),
	/**
	 * 正在盘点
	 */
	COUNTING(2, "正在盘点"),
	/**
	 * 盘点完成
	 */
	COUNT_COMPLETED(3,"盘点完成"),
	/**
	 * 生成差异报告
	 */
	CREATE_COUNT_DIFF(31,"生成差异报告"),
	/**
	 * 作废
	 */
	INVALID(4,"作废"),
	;

	@EnumValue
	@JsonValue
	private final Integer code;
	private final String desc;

	@Override
	public StockCountStateEnum get() {
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
