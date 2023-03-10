package org.nodes.wms.dao.stock.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
	 * 物品编码
	 */
	private String skuCode;
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
	 * LPN
	 */
	private String lpnCode;
	/**
	 * 状态
	 */
	private StockStatusEnum stockStatus;
	/**
	 * 货主
	 */
	private String ownerName;
	/**
	 * 货主ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;
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
	private String balance = "可/余";
	private String storageLocation = "库位";
	private String lot = "批次";
	private String goods = "物品";
	private String goodsName = "名称";
	private String specs = "规格型号";
	/**
	 * 可用
	 */
	private BigDecimal stockEnable;
	/**
	 * 余额
	 */
	private BigDecimal stockBalance;
}
