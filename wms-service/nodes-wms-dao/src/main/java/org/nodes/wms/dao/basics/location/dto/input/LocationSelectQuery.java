package org.nodes.wms.dao.basics.location.dto.input;

import lombok.Data;

/**
 * 库位下拉选择 请求对象
 */
@Data
public class LocationSelectQuery {
	/**
	 * 关键词
	 * 一般为：code,name
	 */
	private String key;
}
