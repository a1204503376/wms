package org.nodes.wms.dao.stock.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * pc拣货返货前端可出库库存dto
 */
@Data
public class PickByPcStockDto {
	/**
	 * 发货单明细id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soDetailId;
	/**
	 * 库存id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
	/**
	 * 可用库存
	 */
	private BigDecimal stockBalance;
	/**
	 * 库存余额
	 */
	private BigDecimal stockEnable;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 批次
	 */
	private String skuLot1;
	/**
	 * 库位编码
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 库区编码
	 */
	private String zoneCode;
	/**
	 * 库房编码
	 */
	private String whCode;
	/**
	 * 箱号
	 */
	private String boxCode;
	/**
	 * 托盘号
	 */
	private String lpnCode;
	/**
	 * 出库数量
	 */
	private BigDecimal outStockQty;
	/**
	 * 序列号集合
	 */
	private List<String> serailList;
	/**
	 * 是否有序列号
	 */
	private Boolean hasSerail;
}
