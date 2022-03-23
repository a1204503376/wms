package org.nodes.wms.core.outstock.so.enums;

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
	Allocated("已分配", 30);

	private String name;
	private int index;
}
