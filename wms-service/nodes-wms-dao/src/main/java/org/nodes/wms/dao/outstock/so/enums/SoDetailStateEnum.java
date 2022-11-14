package org.nodes.wms.dao.outstock.so.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 订单明细状态
 * @create 20200224
 */
@Getter
@AllArgsConstructor
public enum SoDetailStateEnum {
	/**
	 * 波次未分配
	 */
	UnAlloc("波次未分配", 10),
	/**
	 * 已划分波次
	 */
	AllocWellen("已划分波次", 20),
	/**
	 * 已分配
	 */
	Allocated("已分配", 30),
	NORMAL("正常", 0),
	PART("部分拣货", 50),
	ALL_OUT_STOCK("全部拣货", 60),
	DELETED("已删除", 40);

	@JsonValue
	private String desc;

	@EnumValue
	private int code;
}
