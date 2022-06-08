package org.nodes.wms.dao.instock.asn.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 查看明细-ASN单明细表返回对象
 **/
@Data
public class AsnDetailViewResponse implements Serializable {

	private static final long serialVersionUID = 1959979521968969432L;

	/**
	 * ASN单明细ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long asnDetailId;

	/**
	 * 订单行号
	 */
	private String asnLineNo;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 物品规格
	 */
	private String skuSpec;

	/**
	 * 计量单位名称
	 */
	private String umName;

	/**
	 * 计划数量
	 */
	private BigDecimal planQty;

	/**
	 * 实际数量
	 */
	private BigDecimal scanQty;

	/**
	 * 剩余数量
	 */
	private BigDecimal surplusQty;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 接收状态
	 */
	private Integer detailStatus;
}
