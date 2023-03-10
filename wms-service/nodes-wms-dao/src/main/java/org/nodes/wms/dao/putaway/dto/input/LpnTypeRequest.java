package org.nodes.wms.dao.putaway.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 呼叫AGV接收前端箱型参数dto
 */
@Data
public class LpnTypeRequest implements Serializable {
	private static final long serialVersionUID = -9047463340542824476L;
	/**
	 * 箱型
	 */
	private String boxCode;
	/**
	 * 库房id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 库位编码
	 */
	private String locCode;
}
