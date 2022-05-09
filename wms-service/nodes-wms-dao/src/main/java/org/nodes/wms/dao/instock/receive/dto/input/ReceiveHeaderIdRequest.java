package org.nodes.wms.dao.instock.receive.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 收货单接收前端id
 **/
@Data
public class ReceiveHeaderIdRequest implements Serializable {
	private static final long serialVersionUID = 8292243121826445288L;
	@NotNull(message = "ID不能为空")
	private Long receiveId;
}
