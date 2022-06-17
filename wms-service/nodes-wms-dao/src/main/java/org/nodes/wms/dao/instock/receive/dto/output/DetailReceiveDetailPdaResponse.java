package org.nodes.wms.dao.instock.receive.dto.output;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 收货单明细表
 **/
@Data
public class DetailReceiveDetailPdaResponse implements Serializable {
	private static final long serialVersionUID = -2983806351912670754L;
	/**
	 * 收货单明细id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long receiveDetailId;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 计划数量
	 */
	private BigDecimal planQty;
}
