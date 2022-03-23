package org.nodes.core.tool.enums;

/**
 * 批属性是否可见
 * @Author zx
 * @Date 2020/8/3
 **/
public enum ESkuLotDisp {
	TRUE("可见", 1),
	FALSE("不可见", 0);

	private int index;
	private String name;

	private ESkuLotDisp(String name, int index) {
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
