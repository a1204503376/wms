package org.nodes.wms.dao.stock.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 拆箱库存响应对象
 */
@Data
public class DevanningStockResponse implements Serializable {
	private static final long serialVersionUID = 1855271549168247733L;
	/**
	 * 库存id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 批次
	 */
	private String skuLot1;
	/**
	 * 余额
	 */
	private BigDecimal stockBalance;
	/**
	 * 拆箱数
	 */
	private BigDecimal splitQty;
}
