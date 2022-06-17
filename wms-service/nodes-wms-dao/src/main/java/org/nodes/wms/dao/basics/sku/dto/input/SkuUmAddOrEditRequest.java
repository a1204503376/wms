package org.nodes.wms.dao.basics.sku.dto.input;

import lombok.Data;

/**
 * 计量单位修改或新增request
 */
@Data
public class SkuUmAddOrEditRequest {
	/**
	 * 计量单位编码
	 */
	private String	wsuCode;

	/**
	 * 计量单位名称
	 */
	private String wsuName;
}
