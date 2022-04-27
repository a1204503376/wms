package org.nodes.wms.dao.receive.detail.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 收货单明细接收类
 **/
@Data
public class DetailRequest implements Serializable {
	private Long receiveDetailId;
	private BigDecimal scanQty;
}
