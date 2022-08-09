package org.nodes.wms.dao.outstock.logSoPick.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * Pda根据发货单ID查询出库单明细-请求对象
 **/
@Data
public class FindOpenSoDetailRequest implements Serializable {
	private static final long serialVersionUID = -2328658218530094518L;
	/**
	 * 发货单id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
	/**
	 * 库房id
	 */
	private Long whId;
}
