package org.nodes.wms.dao.instock.asn.dto.input;

import lombok.Data;
import org.nodes.core.tool.validation.Update;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Asn单编辑参数
 **/
@Data
public class AddOrEditAsnBillRequest implements Serializable {

	private static final long serialVersionUID = -6185516788898062120L;

	/**
	 * ASN单id
	 */
	@NotNull(message = "Asn单id不能为空",groups = Update.class)
	private Long asnBillId;

	/**
	 * ASN单明细被删除记录的 id
	 */
	private List<Long> removeIdList;

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
	private List<AsnDetailRequest> asnDetailList;
}
