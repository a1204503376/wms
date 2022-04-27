package org.nodes.wms.dao.application.dto.output;

import lombok.Data;

/**
 * 通用状态返回对象
 * 使用场景：
 * 1.前端下拉框使用
 */
@Data
public class StateGeneralResponse {

	/**
	 * 标签
	 */
	private String label;
	/**
	 * 值
	 * 一般对应数据存储的状态值
	 */
	private Integer value;
}
