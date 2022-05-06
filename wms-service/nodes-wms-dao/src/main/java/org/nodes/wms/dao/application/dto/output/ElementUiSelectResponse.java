package org.nodes.wms.dao.application.dto.output;

import lombok.Data;

/**
 * ElementUI 下拉框 返回对象
 */
@Data
public class ElementUiSelectResponse<T> {

	/**
	 * 标签
	 */
	private String label;
	/**
	 * 值
	 * 一般对应数据存储的状态值
	 */
	private T value;
}
