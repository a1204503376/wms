package org.nodes.wms.dao.count.dto.output;

import lombok.Data;

import java.math.BigDecimal;

/**
 * PDA显示箱号和箱子内物品总数量的返回对象
 */
@Data
public class PdaBoxQtyResponse {

	/**
	 * 箱号
	 */
	private String boxCode;

	/**
	 * 箱内物品总数量
	 */
	private BigDecimal totalQty;
}
