package org.nodes.wms.dao.basics.sku.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 物品下拉选择 返回对象
 */
@Data
public class SkuSelectRequest implements Serializable {
	private static final long serialVersionUID = 21290733663648044L;
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

	/**
	 * 物品规格
	 */
	private String skuSpec;
}
