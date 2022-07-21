package org.nodes.wms.dao.basics.sku.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 物品id请求类
 **/
@Data
public class SkuIdRequest implements Serializable {

	private static final long serialVersionUID = -6335984446803325870L;

	/**
	 * 物品id
	 */
	private Long skuId;
}
