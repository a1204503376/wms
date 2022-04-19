package org.nodes.wms.dao.instock.asn.dto.output;

import lombok.Data;

import java.util.Date;

/**
 * ASN单分页结果
 */
@Data
public class PageResponse {
	/**
	 * ASN单ID
	 */
	private Long asnBillId;
	/**
	 * ASN单编码
	 */
	private String asnBillNo;
	/**
	 * 物料编码
	 */
	private String skuCode;
	/**
	 * 创建时间
	 */
	private Date createTime;
}
