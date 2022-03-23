package org.nodes.core.tool.enums;

/**
 * 批属性是否必填
 * @Author zx
 * @Date 2020/8/3
 **/
public enum ESkuLotMust {
	TRUE("必须", 1),
	FALSE("非必须", 0);

	private int index;
	private String name;

	private ESkuLotMust(String name, int index) {
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
