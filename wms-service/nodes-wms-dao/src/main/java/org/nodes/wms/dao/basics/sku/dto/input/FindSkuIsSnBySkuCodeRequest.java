package org.nodes.wms.dao.basics.sku.dto.input;

import lombok.Data;

import java.io.Serializable;

@Data
public class FindSkuIsSnBySkuCodeRequest implements Serializable {
	private static final long serialVersionUID = 3841242546508057617L;
	/**
	 * 物品编码
	 */
	private String skuCode;
}
