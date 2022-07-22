package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 库存移动请求对象
 */
@Data
public class StockMoveRequest implements Serializable {
	private static final long serialVersionUID = 6251527073571241801L;
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
	/**
	 * 目标编码
	 */
	private String targetLocCode;
	/**
	 * 序列号
	 */
	private List<String> serialNumberList;
}
