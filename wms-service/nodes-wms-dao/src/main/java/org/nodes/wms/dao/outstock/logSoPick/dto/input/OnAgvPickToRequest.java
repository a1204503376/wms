package org.nodes.wms.dao.outstock.logSoPick.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 接驳区拣货请求对象
 *
 * @author nodesc
 */
@Data
public class OnAgvPickToRequest implements Serializable {
	private static final long serialVersionUID = -2835186246027646361L;
	/**
	 * 库位ID
	 */
	private Long locId;
	/**
	 * 库位编码
	 */
	private String locCode;
}
