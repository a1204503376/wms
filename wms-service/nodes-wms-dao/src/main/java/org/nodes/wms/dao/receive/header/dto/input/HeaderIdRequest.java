package org.nodes.wms.dao.receive.header.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 收货单接收前端id
 **/
@Data
public class HeaderIdRequest {
	@NotNull(message = "ID不能为空")
	private Long receiveId;
}
