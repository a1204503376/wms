package org.nodes.wms.dao.instock.receive.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 收货单明细接收类
 **/
@Data
public class ReceiveDetailRequest implements Serializable {
	private static final long serialVersionUID = 4738364170869475524L;
	private Long receiveDetailId;
	private BigDecimal scanQty;
}
