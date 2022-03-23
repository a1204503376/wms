package org.nodes.wms.core.count.enums;
/**
 * @description 盘点库位状态
 *
 * @author pengwei
 * @since 2020-07-03
 */
public enum LocStatusEnum {

	UnAllot("未分配", 10),
	UnComplate("未完成", 11),
	Complated("已完成", 12);

	private int index;
	private String name;

	private LocStatusEnum(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public int getIndex() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
