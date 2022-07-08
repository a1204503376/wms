package org.nodes.wms.dao.putway.dto.output;

import lombok.Data;

/**
 * 呼叫AGV返回前端库位信息dto
 */
@Data
public class LocResponse {
	/**
	 * 库位id
	 */
	private Long locId;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 *  库位是否为空
	 */
	private Boolean isEmpty;
}
