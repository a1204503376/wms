package org.nodes.wms.dao.outstock.so.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发货单分配页面明细信息响应类
 **/
@Data
public class SoDetailForDistResponse implements Serializable {

	private static final long serialVersionUID = 8609062416880872076L;

	/**
	 * 发货单明细id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soDetailId;

	/**
	 * 行号
	 */
	private String soLineNo;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 剩余量
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private BigDecimal surplusQty;

	/**
	 * 计划数量
	 */
	private BigDecimal planQty;

	/**
	 * 计量单位编码
	 */
	private String umCode;
}