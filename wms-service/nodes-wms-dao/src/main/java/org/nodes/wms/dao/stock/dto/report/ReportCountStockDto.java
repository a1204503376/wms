package org.nodes.wms.dao.stock.dto.report;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 报表 盘点单Dto类
 **/
@Data
public class ReportCountStockDto {

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 型号
	 */
	private String skuLot2;

	/**
	 * 库存余额汇总
	 */
	private BigDecimal stockBalanceTotal;
}
