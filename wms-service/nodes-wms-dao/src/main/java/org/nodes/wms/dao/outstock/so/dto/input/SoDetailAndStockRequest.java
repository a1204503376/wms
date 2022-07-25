package org.nodes.wms.dao.outstock.so.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * pc拣货查询收货单明细和库存request
 */
@Data
public class SoDetailAndStockRequest {
	/**
	 * 发货单id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
	/**
	 * 行号
	 */
	private String soLineNo;
	/**
	 * 库房id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
}
