package org.nodes.wms.dao.instock.receiveLog.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 撤销收货请求dto类
 **/
@Data
public class ReceiveCancelRequest implements Serializable {

	private static final long serialVersionUID = 678817513415594863L;

//	@NotNull(message = "至少传入一个id")
	private List<Long> idList;
}
