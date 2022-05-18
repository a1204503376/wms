package org.nodes.wms.dao.instock.asn.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierSelectResponse;

import java.io.Serializable;

/**
 * 编辑-头表返回对象
 **/
@Data
public class AsnHeaderEditResponse implements Serializable {

	private static final long serialVersionUID = 4946312586187022401L;

	/**
	 * ASN单id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long asnBillId;

	/**
	 * ASN单编码
	 */
	private String asnBillNo;

	/**
	 * 单据类型编码
	 */
	private String billTypeCd;

	/**
	 * 供应商
	 */
	private SupplierSelectResponse supplierSelectResponse;

	/**
	 * 仓库id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * ASN单头表备注
	 */
	private String asnBillRemark;
}
