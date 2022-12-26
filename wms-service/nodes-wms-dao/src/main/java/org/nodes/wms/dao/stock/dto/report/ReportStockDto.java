package org.nodes.wms.dao.stock.dto.report;

import lombok.Data;
import org.springblade.core.tool.utils.ConvertUtil;

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
	 * 物品分类id
	 */
	private Long skuTypeId;

	/**
	 * 占用库存总数
	 */
	private BigDecimal occupyQtySum;
	public void setOccupyQtySum(BigDecimal occupyQtySum) {
		this.occupyQtySum = ConvertUtil.convert(occupyQtySum.stripTrailingZeros().toPlainString(), BigDecimal.class) ;
	}
	/**
	 * 可发货库存总数
	 */
	private BigDecimal stockEnableSum;
	public void setStockEnableSum(BigDecimal stockEnableSum) {
		this.stockEnableSum = ConvertUtil.convert(stockEnableSum.stripTrailingZeros().toPlainString(), BigDecimal.class);
	}

	/**
	 * 即时库存总数
	 */
	private BigDecimal normalStockSum;
	public void setNormalStockSum(BigDecimal normalStockSum) {
		this.normalStockSum = ConvertUtil.convert(normalStockSum.stripTrailingZeros().toPlainString(), BigDecimal.class);
	}

	/**
	 * 冻结库存总数
	 */
	private BigDecimal freezeStockSum;
	public void setFreezeStockSum(BigDecimal freezeStockSum) {
		this.freezeStockSum = ConvertUtil.convert(freezeStockSum.stripTrailingZeros().toPlainString(), BigDecimal.class);
	}
}
