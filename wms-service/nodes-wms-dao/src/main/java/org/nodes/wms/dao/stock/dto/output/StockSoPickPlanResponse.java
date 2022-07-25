package org.nodes.wms.dao.stock.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发货单分配页面可分配库存响应类
 **/
@Data
public class StockSoPickPlanResponse extends BaseSkuLot implements Serializable {

	private static final long serialVersionUID = -829789935189414879L;

	/**
	 * 库存id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;

	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * 库位id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;

	/**
	 * 库位
	 */
	private String locName;

	/**
	 * 库区id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long zoneId;

	/**
	 * 库区
	 */
	private String zoneName;

	/**
	 * lpn
	 */
	private String lpnCode;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 批次号
	 */
	private String lotNumber;

	/**
	 * 可用量
	 */
	private BigDecimal stockEnableQty;

	/**
	 * 余额
	 */
	private BigDecimal stockBalanceQty;

	/**
	 * 库存状态
	 */
	private String stockState;
}
