package org.nodes.core.tool.enums;

/**
 * 批属性是否允许混合存放
 * @Author zx
 * @Date 2020/8/3
 **/
public enum ESkuLotMix {
	TRUE("允许", 1),
	FALSE("不允许", 0);

	private int index;
	private String name;

	private ESkuLotMix(String name, int index) {
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
