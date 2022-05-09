package org.nodes.wms.dao.instock.asn.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Asn单编辑参数
 **/
@Data
public class EditAsnBillRequest implements Serializable {

	private static final long serialVersionUID = -6185516788898062120L;

	/**
	 * Asn单id
	 */
	@NotNull(message = "Asn单id不能为空")
	private Long asnBillId;

	/**
	 * 单据类型
	 */
	@NotNull(message = "单据类型不能为空")
	private Integer billTypeCd;

	/**
	 * 供应商编码
	 */
	@NotNull(message = "供应商编码不能为空")
	private String supplierCode;

	/**
	 * 供应商名称
	 */
	@NotNull(message = "供应商名称不能为空")
	private String supplierName;

	/**
	 * 仓库编码
	 */
	private String whCode;

	/**
	 * 仓库名称
	 */
	private String whName;

	/**
	 * 备注
	 */
	private String asnBillRemark;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 *物品名称
	 */
	private String skuName;

	/**
	 * 计量单位编码
	 */
	private String umCode;

	/**
	 * 计量单位名称
	 */
	private String umName;

	/**
	 * 基础计量单位编码
	 */
	private String baseUmCode;

	/**
	 * 基础计量单位名称
	 */
	private String baseUmName;

	/**
	 * 计划数量
	 */
	private BigDecimal planQty;

	/**
	 * 已收数量
	 */
	private BigDecimal scanQty;

	/**
	 * 备注
	 */
	private String remark;
}
