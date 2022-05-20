package org.nodes.wms.dao.instock.asn.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 编辑-ASN单明细dto
 **/
@Data
public class AsnDetailRequest implements Serializable {

	private static final long serialVersionUID = 2434526882649138036L;

	/**
	 * ASN单id
	 */
	private Long asnDetailId;

	/**
	 * 行号
	 */
	private String asnLineNo;

	/**
	 * 物品id
	 */
	private Long skuId;

	/**
	 * 计量单位编码
	 */
	private String umCode;

	/**
	 * 计划数量
	 */
	private BigDecimal planQty;

	/**
	 * 备注
	 */
	private String remark;
}
