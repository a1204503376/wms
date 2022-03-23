package org.nodes.wms.core.log.system.enums;

/**
 * @author pengwei
 * @program WmsCore
 * @description 平台枚举
 * @create 20200310
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
