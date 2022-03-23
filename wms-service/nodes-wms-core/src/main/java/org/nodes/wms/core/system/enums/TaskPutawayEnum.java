package org.nodes.wms.core.system.enums;
/**
 * @description 任务：上架执行方式配置
 *
 * @author pengwei
 * @since 2020-06-22
 */
public enum TaskPutawayEnum {
	Lpn("按托上架", 0),
	Piece("按件上架", 1),
	Box("按箱上架", 2);

	private Integer value;

	private String name;

	private TaskPutawayEnum(String name, Integer value) {
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
