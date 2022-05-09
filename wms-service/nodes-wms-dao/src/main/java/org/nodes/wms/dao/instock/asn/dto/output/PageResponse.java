package org.nodes.wms.dao.instock.asn.dto.output;

import lombok.Data;

import java.io.Serializable;

/**
 * ASN单分页结果
 */
@Data
public class PageResponse
	extends AsnBillExportResponse
	implements Serializable {

	private static final long serialVersionUID = 8705509654116950416L;

	/**
	 * ASN单主键id
	 */
	private Long asnBillId;

}
