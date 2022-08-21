package org.nodes.wms.dao.outstock.logSoPick.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 接驳区移动请求对象
 *
 * @author nodesc
 */
@Data
public class MoveOnAgvPickToRequest implements Serializable {
	private static final long serialVersionUID = -2835186246027646361L;
	/**
	 * 原库位ID
	 */
	private Long sourceLocId;

	/**
	 * 原库位编码
	 */
	private String sourceLocCode;

	/**
	 * 目标库位编码
	 */
	private String targetLocCode;

	/**
	 * 库房ID
	 */
	private Long whId;
}
