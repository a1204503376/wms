package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class FindIsSnByStockIdRequest implements Serializable {
	private static final long serialVersionUID = 8426662604328035856L;
	/**
	 * 库存ID
	 */
	private Long stockId;
	/**
	 * 箱码
	 */
	private String boxCode;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 批次
	 */
	private String skuLot1;
	/**
	 * 数量
	 */
	private BigDecimal qty;
}
