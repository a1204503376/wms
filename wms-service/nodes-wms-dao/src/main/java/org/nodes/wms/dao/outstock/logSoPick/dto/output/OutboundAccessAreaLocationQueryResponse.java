package org.nodes.wms.dao.outstock.logSoPick.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 出库接驳区拣货库位查询响应对象
 */
@Data
public class OutboundAccessAreaLocationQueryResponse implements Serializable {
	private static final long serialVersionUID = -8860028173103848186L;
	/**
	 * 库位ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;

	/**
	 * 库位编码
	 */
	private String locCode;

	public String getLocCodeView() {
		return locCode.substring(11);
	}
}
