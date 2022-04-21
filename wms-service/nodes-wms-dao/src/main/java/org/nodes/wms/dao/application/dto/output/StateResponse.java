package org.nodes.wms.dao.application.dto.output;

import lombok.Data;

/**
 * 状态
 * 返回给前端下拉框使用的对象
 */
@Data
public class StateResponse {
	/**
	 * 标签
	 */
	private String label;
	/**
	 * 值
	 * 一般对应数据存储的状态值
	 */
	private String value;
}
