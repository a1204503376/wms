package org.nodes.wms.dao.putway.dto.input;

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
	private String lpnType;
	/**
	 * 库房id
	 */
	private Long whId;
}
