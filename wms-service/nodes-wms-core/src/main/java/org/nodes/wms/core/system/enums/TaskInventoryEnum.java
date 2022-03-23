package org.nodes.wms.core.system.enums;
/**
 * @description 任务：收货执行方式配置
 *
 * @author pengwei
 * @since 2020-06-22
 */
public enum TaskInventoryEnum {

	Lpn("按托收货", 0),
	Piece("按件收货", 1),
	Box("按箱收货", 2);

	private Integer value;

	private String name;

	private TaskInventoryEnum(String name, Integer value) {
		this.name = name;
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
