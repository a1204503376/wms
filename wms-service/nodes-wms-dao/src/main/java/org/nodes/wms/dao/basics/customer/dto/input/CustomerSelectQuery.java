package org.nodes.wms.dao.basics.customer.dto.input;

import lombok.Data;

/**
 * 客户下拉选择 请求对象
 */
@Data
public class CustomerSelectQuery {
	/**
	 * 关键词
	 * 一般为：code,name
	 */
	private String key;
}
