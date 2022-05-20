package org.nodes.wms.dao.instock.asn.dto.input;

import lombok.Data;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;


/**
 * ASN单创建请求对象
 */
@Data
public class AddAsnBillRequest implements Serializable {

	private static final long serialVersionUID = 4274547396438773726L;

	/**
	 * 单据类型
	 */
	@NotNull(message = "单据类型不能为空")
	private String billTypeCd;

	/**
	 * 供应商id
	 */
	private Long supplierId;

	/**
	 * 仓库id
	 */
	private Long whId;

	/**
	 * 货主id
	 */
	private Long woId;

	/**
	 * ASN单备注
	 */
	private String asnBillRemark;

	/**
	 * ASN单明细
	 */
	@NotNull(message = "ASN单明细不能为空")
	@Size(min = 1,message = "ASN单明细不能为空")
	private List<AsnDetail> asnDetailList;
}
