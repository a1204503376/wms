package org.nodes.wms.dao.instock.asn.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * ASN单查看详情返回对象
 **/
@Data
public class AsnHeaderForDetailResponse implements Serializable {

	private static final long serialVersionUID = -3473344917539589477L;

	/**
	 * ASN单头表id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long asnBillId;

	/**
	 * 单据编码
	 */
	private String asnBillNo;

	/**
	 * 单据类型名称
	 */
	private String billTypeName;

	/**
	 * 供应商名称
	 */
	private String supplierName;

	/**
	 * 仓库名称
	 */
	private String whName;

	/**
	 * 货主名称
	 */
	private String ownerName;

	/**
	 * 备注
	 */
	private String asnBillRemark;

	/**
	 * ASN单头表信息dto
	 */
//	private AsnHeaderViewResponse asnHeaderViewResponse;

}
