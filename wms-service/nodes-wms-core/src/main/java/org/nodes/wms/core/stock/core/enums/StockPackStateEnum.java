package org.nodes.wms.core.stock.core.enums;
/**
 * @description 尾箱打包状态
 *
 * @author pengwei
 * @since 2020-07-07
 */
public enum StockPackStateEnum {
	Default("已下发", 0),
	UnPack("待打包", 10),
	PartPack("部分打包", 15),
	Complated("打包完成", 20),
	Cancel("已取消", 30);

	private int index;
	private String name;

	private StockPackStateEnum(String name, int index) {
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
