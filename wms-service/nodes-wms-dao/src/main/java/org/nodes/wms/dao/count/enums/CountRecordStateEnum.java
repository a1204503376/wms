package org.nodes.wms.dao.count.enums;

/**
 * @author pengwei
 * @program WmsCore
 * @description 盘点记录状态
 * @create 20200228
 */
public enum CountRecordStateEnum {
	Create("已盘点", 1),
	Repeal("已撤销", 2),
	Complated("已生成", 3);

	private int index;
	private String name;

	private CountRecordStateEnum(String name, int index) {
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
