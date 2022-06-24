package org.nodes.wms.dao.instock.receiveLog.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 首页入库清点记录
 **/
@Data
public class ReceiveLogIndexResponse implements Serializable {

	private static final long serialVersionUID = 1180263188335022323L;

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
	 * 入库量
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private BigDecimal qty;
}
