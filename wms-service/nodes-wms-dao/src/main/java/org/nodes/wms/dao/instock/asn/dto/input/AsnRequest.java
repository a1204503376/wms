package org.nodes.wms.dao.instock.asn.dto.input;

import lombok.Data;

/**
 * ASN单创建请求对象
 */
@Data
public class AsnRequest {
	private String skuCode;
	private String skuName;
}
