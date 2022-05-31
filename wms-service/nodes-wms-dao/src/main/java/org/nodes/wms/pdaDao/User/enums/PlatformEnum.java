package org.nodes.wms.pdaDao.User.enums;

/**
 * @description 平台枚举
 */
public enum PlatformEnum {
	Web("WEB", 10),
	PC("PC", 20),
	PDA("PDA", 30),
	Android("Android", 40),
	IOS("IOS", 50);

	private int index;
	private String name;

	private PlatformEnum(String name, int index) {
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

