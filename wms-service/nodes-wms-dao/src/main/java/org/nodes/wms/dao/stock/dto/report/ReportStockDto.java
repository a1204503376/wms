package org.nodes.wms.dao.stock.dto.report;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 报表 实时库存明细Dto类
 **/
@Data
public class ReportStockDto {
	/**
	 * 物品名称
	 */
	private String skuName;
	/**
	 * 物品规格
	 */
	private String skuLot2;
	/**
	 * 计量单位名称
	 */
	private String wsuName;
	/**
	 * 专用客户
	 */
	private String skuLot4;
	/**
	 * 占用库存总数
	 */
	private BigDecimal occupyQtySum;
	/**
	 * 可发货库存总数
	 */
	private BigDecimal stockEnableSum;
	/**
	 * 即时库存总数
	 */
	private BigDecimal normalStockSum;
	/**
	 * 冻结库存总数
	 */
	private BigDecimal freezeStockSum;
}
