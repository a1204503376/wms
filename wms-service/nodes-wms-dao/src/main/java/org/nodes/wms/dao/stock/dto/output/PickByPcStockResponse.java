package org.nodes.wms.dao.stock.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;

/**
 * pc拣货返货前端可出库库存dto
 */
@Data
public class PickByPcStockResponse {
	/**
	 * 发货单明细id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soDetailId;
	/**
	 * 可用库存
	 */
	private BigDecimal stockEnableQty;
	/**
	 * 库存余额
	 */
	private BigDecimal stockBalanceQty;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 批次号
	 */
	private String lotNumber;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 库区编码
	 */
	private String zoneCode;
	/**
	 * 箱号
	 */
	private String boxCode;
	/**
	 * 托盘号
	 */
	private String lpnCode;
}
