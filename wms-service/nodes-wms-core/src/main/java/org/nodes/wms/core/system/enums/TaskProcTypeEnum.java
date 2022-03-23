package org.nodes.wms.core.system.enums;

/**
 * @author pengwei
 * @program WmsCore
 * @description 任务执行方式枚举
 * @create 20200214
 */
public enum TaskProcTypeEnum {

	None("不发布任务", 0),
	Cooperation("协作模式", 90),
	Rob("抢单模式", 91),
	Artificial("人工分派模式", 92),
	Auto("自动分派模式", 93),
	Backstage("后台任务", 99);

	private int index;
	private String name;

	private TaskProcTypeEnum(String name, int index) {
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
