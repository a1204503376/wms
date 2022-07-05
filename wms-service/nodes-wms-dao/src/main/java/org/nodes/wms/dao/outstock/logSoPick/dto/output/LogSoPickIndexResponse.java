package org.nodes.wms.dao.outstock.logSoPick.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 首页拣货记录dto类
 **/
@Data
public class LogSoPickIndexResponse implements Serializable {

	private static final long serialVersionUID = 1494629186825609551L;

	/**
	 * 物品id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 出库量
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private BigDecimal pickRealQty;
}
