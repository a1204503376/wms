package org.nodes.wms.dao.basics.sku.enums;
/**
 * @description 包装层级
 *
 * @author pengwei
 * @since 2020-07-03
 */
public enum SkuLevelEnum {
	Base("基础计量单位", 1),
	Inner("内包装", 10),
	Box("箱", 20),
	Plate("托盘", 30);

	private int index;
	private String name;

	private SkuLevelEnum(String name, int index) {
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
