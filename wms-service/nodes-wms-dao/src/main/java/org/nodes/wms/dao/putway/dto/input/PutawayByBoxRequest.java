package org.nodes.wms.dao.putway.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 按箱上架请求对象
 **/
@Data
public class PutawayByBoxRequest implements Serializable {

	private static final long serialVersionUID = 5278548007801352620L;
	/**
	 * 箱码
	 */
	private String boxCode;
	/**
	 * 库房id
	 */
	private Long whId;
}
