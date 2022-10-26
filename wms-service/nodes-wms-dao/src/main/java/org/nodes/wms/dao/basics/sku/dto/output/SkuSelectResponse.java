package org.nodes.wms.dao.basics.sku.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 物品下拉选择 返回对象
 */
@Data
public class SkuSelectResponse {

	/**
	 * 物品主键ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
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
	 * 规格
	 */
	private String skuSpec;
}
