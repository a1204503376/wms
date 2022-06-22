package org.nodes.wms.dao.instock.receive.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 收货单详情接收前端请求条件
 */
@Data
public class ReceiveDetailByReceiveIdPdaQuery {
	/**
	 * 收货明细单主键id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long receiveDetailId;
}
