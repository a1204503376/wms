package org.nodes.wms.dao.stock.dto.report;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 报表 库存Dto类
 **/
@Data
public class ReportAgeOfInventoryDto {

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 规格型号
	 */
	private String skuLot2;

	/**
	 * 主计量单位
	 */
	private String wsuName;

	/**
	 * 期末结存数量
	 */
	private BigDecimal finalBalanceQty;

	/**
	 * 小于1年数量
	 */
	private BigDecimal less1yearQty;

	/**
	 * 1-2年数量
	 */
	private BigDecimal between1and2yearQty;

	/**
	 * 2-3年数量
	 */
	private BigDecimal between2and3yearQty;

	/**
	 * 3-4年数量
	 */
	private BigDecimal between3and4yearQty;

	/**
	 * 4-5年数量
	 */
	private BigDecimal between4and5yearQty;

	/**
	 * 5年以上数量
	 */
	private BigDecimal greater5yearQty;
}
