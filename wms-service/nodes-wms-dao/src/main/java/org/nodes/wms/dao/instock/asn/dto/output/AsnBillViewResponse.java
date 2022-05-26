package org.nodes.wms.dao.instock.asn.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ASN单查看详情返回对象
 **/
@Data
public class AsnBillViewResponse implements Serializable {

	private static final long serialVersionUID = -3473344917539589477L;

	/**
	 * ASN单头表信息dto
	 */
	private AsnHeaderViewResponse asnHeaderViewResponse;

	/**
	 * ASN单明细表信息dto
	 */
	private List<AsnDetailViewResponse> asnDetailViewResponse;
}
