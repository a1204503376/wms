package org.nodes.wms.dao.stock.dto.report;

import lombok.Data;
import org.springblade.core.tool.utils.ConvertUtil;

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
	 * 小于半年数量
	 */
	private BigDecimal less05yearQty;

	/**
	 * 0.5-1年数量
	 */
	private BigDecimal between05and1yearQty;

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

	public void setFinalBalanceQty(BigDecimal finalBalanceQty) {
		this.finalBalanceQty = ConvertUtil.convert(finalBalanceQty.stripTrailingZeros().toPlainString(), BigDecimal.class);
	}

	public void setLess05yearQty(BigDecimal less05yearQty) {
		this.less05yearQty = ConvertUtil.convert(less05yearQty.stripTrailingZeros().toPlainString(), BigDecimal.class);
	}

	public void setBetween05and1yearQty(BigDecimal between05and1yearQty) {
		this.between05and1yearQty = ConvertUtil.convert(between05and1yearQty.stripTrailingZeros().toPlainString(), BigDecimal.class);
	}

	public void setBetween1and2yearQty(BigDecimal between1and2yearQty) {
		this.between1and2yearQty = ConvertUtil.convert(between1and2yearQty.stripTrailingZeros().toPlainString(), BigDecimal.class);
	}

	public void setBetween2and3yearQty(BigDecimal between2and3yearQty) {
		this.between2and3yearQty = ConvertUtil.convert(between2and3yearQty.stripTrailingZeros().toPlainString(), BigDecimal.class);
	}

	public void setBetween3and4yearQty(BigDecimal between3and4yearQty) {
		this.between3and4yearQty = ConvertUtil.convert(between3and4yearQty.stripTrailingZeros().toPlainString(), BigDecimal.class);
	}

	public void setBetween4and5yearQty(BigDecimal between4and5yearQty) {
		this.between4and5yearQty = ConvertUtil.convert(between4and5yearQty.stripTrailingZeros().toPlainString(), BigDecimal.class);
	}

	public void setGreater5yearQty(BigDecimal greater5yearQty) {
		this.greater5yearQty = ConvertUtil.convert(greater5yearQty.stripTrailingZeros().toPlainString(), BigDecimal.class);
	}
}
