package org.nodes.wms.dao.application.dto.input;

import lombok.Data;

/**
 * 针对ASN单进行收货的请求对象
 */
@Data
public class ReceiveForAsnRequest {

	/**
	 * 物料编码
	 */
	private String skuCode;
}
