package org.nodes.wms.dao.basics.location.dto.output;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询库位的返回对象
 **/
@Data
public class LocationPdaByPcsResponse implements Serializable {
	private static final long serialVersionUID = 294694183834424313L;

	/**
	 * 库房id
	 */
	private Long whId;

	/**
	 * 库位编码
	 */
	private String locCode;
}
