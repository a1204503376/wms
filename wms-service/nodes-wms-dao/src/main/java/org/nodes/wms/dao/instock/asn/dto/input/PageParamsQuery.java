package org.nodes.wms.dao.instock.asn.dto.input;

import lombok.Data;

/**
 * ASN单据 分页参数
 */
@Data
public class PageParamsQuery {
	/**
	 * ASN单编码
	 */
	private String asnBillNo;
	/**
	 * 物料编码
	 */
	private String skuCode;
	/**
	 * 状态
	 */
	private String asnBillState;
}
