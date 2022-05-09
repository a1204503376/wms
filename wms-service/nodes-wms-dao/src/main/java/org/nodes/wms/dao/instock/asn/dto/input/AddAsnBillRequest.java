package org.nodes.wms.dao.instock.asn.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


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
	private Long whId;

	/**
	 * 备注
	 */
	private String asnBillRemark;

	/**
	 * 行号
	 */
	private String asnLineNo;

	/**
	 * 物品编码
	 */
	@NotNull(message="物品编码不能为空")
	private String skuCode;

	/**
	 * 物品编码
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
	@NotNull(message="计划数量不能为空")
	private BigDecimal planQty;

	/**
	 * 备注
	 */
	private String remark;
}
