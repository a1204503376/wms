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
	 * 占用数量总数
	 */
	private BigDecimal occupyQtySum;
	/**
	 * 可用数量总数
	 */
	private BigDecimal stockEnableSum;
	/**
	 * 正常库存总数
	 */
	private BigDecimal normalStockSum;
	/**
	 * 冻结库存总数
	 */
	private BigDecimal freezeStockSum;
}
