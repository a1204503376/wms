package org.nodes.wms.dao.instock.receive.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 收货单接收前端请求条件
 */
@Data
public class ReceiveDetailPdaQuery {
	/**
	 * 收货单主键ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long receiveId;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 库房id
	 */
	private Long whId;
}
