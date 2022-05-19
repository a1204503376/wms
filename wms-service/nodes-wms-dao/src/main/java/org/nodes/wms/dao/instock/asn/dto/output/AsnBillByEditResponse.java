package org.nodes.wms.dao.instock.asn.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑返回对象
 **/
@Data
public class AsnBillByEditResponse implements Serializable {

	private static final long serialVersionUID = 2926311540120578861L;

	/**
	 * ASN单部分  包括单据类型dto、供应商dto、单据类型id
	 */
	private AsnHeaderEditResponse asnHeaderEditResponse;

	/**
	 * ASN单明细部分
	 */
	private List<AsnDetailEditResponse> asnDetailEditResponseList;
}
