package org.nodes.wms.core.log.system.enums;

/**
 * @description EStockPackProcType
 *
 * @author pengwei
 * @since 2020-07-13
 */
public enum StockPackProcTypeEnum {

	Normal("任务下发", 0),
	Pick("尾箱拣货", 10),
	Pack("尾箱打包", 20);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	private String name;
	private Integer index;

	private StockPackProcTypeEnum(String name, Integer index) {
		this.name = name;
		this.index = index;
	}
}
