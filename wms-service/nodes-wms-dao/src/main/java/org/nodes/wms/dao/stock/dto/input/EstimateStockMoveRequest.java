package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 判断库存是否可移动请求对象
 */
@Data
public class EstimateStockMoveRequest implements Serializable {
	private static final long serialVersionUID = 1855271549168247733L;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 批次号
	 */
	private String lotNumber;
	/**
     * 数量
	 */
	private BigDecimal qty;
}
