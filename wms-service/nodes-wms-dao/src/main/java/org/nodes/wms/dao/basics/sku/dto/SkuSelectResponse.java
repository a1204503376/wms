package org.nodes.wms.dao.basics.sku.dto;

import lombok.Data;

/**
 * 物品下拉选择 返回对象
 */
@Data
public class SkuSelectResponse {

	/**
	 * 物品主键ID
	 */
	private Long skuId;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;
}
