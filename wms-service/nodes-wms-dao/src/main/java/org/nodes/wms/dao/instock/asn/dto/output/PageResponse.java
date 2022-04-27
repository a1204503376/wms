package org.nodes.wms.dao.instock.asn.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ASN单分页结果
 */
@Data
public class PageResponse implements Serializable {

	private static final long serialVersionUID = 8705509654116950416L;

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
