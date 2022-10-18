package org.nodes.wms.dao.outstock.logSoPick.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

@Data
public class FindBoxCountBySoHeaderIdRequest implements Serializable {

	private static final long serialVersionUID = -7823945045835368844L;
	/**
	 * 发货单ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
}
