package org.nodes.wms.dao.stock.dto.output;

import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Pda根据编码查询库存-响应对象
 */
@Data
public class FindAllStockByNoResponse extends BaseSkuLot implements Serializable {
	private static final long serialVersionUID = 5677449884106430903L;
	/**
	 * 物品名称
	 */
	private String skuName;
	/**
	 * 可用余额(单位)
	 */
	private BigDecimal qty;
	/**
	 * 批次
	 */
	private String lotNumber;
	/**
	 * 箱码
	 */
	private String boxCode;
	/**
	 * 库位
	 */
	private String locCode;
	/**
	 * 状态
	 */
	private StockStatusEnum stockStatus;
	/**
	 * 货主
	 */
	private String ownerName;
	/**
	 * 计量单位编码
	 */
	private String wsuCode;
	/**
	 * 上架数量
	 */
	private BigDecimal stockQty;
	/**
	 * 下架数量
	 */
	private BigDecimal pickQty;

	public String getQty() {
		return this.getStockQty().subtract(this.getPickQty()) + wsuCode;
	}
}
