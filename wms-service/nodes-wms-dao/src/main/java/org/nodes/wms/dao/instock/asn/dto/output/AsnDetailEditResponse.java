package org.nodes.wms.dao.instock.asn.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.basics.sku.dto.SkuSelectResponse;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 编辑-ASN单明细返回对象
 **/
@Data
public class AsnDetailEditResponse implements Serializable {

	private static final long serialVersionUID = 5065302353039921L;

	/**
	 * ASN单明细id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long asnDetailId;

	/**
	 * 行号
	 */
	private String asnLineNo;

	/**
	 * 物品组件dto
	 */
	private SkuSelectResponse skuSelectResponse;

	/**
	 * 计划数量
	 */
	private BigDecimal planQty;

	/**
	 * 已收数量
	 */
	private BigDecimal scanQty;

	/**
	 * ASN单明细备注
	 */
	private String remark;
}
