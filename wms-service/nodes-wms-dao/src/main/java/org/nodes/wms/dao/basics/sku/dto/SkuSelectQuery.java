package org.nodes.wms.dao.basics.sku.dto;

import lombok.Data;

/**
 * 物品下拉选择 请求对象
 */
@Data
public class SkuSelectQuery {

	/**
	 * 关键词
	 * 一般为：skuCode,skuName
	 */
	private String key;
}
