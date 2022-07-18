package org.nodes.wms.dao.instock.receive.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * pc收货接收前端参数dto
 */
@Data
public class ReceiveByPcRequest {
	/**
	 * 收货单主键id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long receiveId;
	/**
	 * 明细集合
	 */
	List<ReceiveByPcDetailRequest> detailRequestList;
}
