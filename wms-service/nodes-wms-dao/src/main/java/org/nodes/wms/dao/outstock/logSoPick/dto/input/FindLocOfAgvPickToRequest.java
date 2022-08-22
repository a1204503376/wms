package org.nodes.wms.dao.outstock.logSoPick.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 出库接驳区拣货库位查询请求条件
 */
@Data
public class FindLocOfAgvPickToRequest implements Serializable {
	private static final long serialVersionUID = -7875005886358189L;
	/**
	 * 库房ID
	 */
	private Long whId;
}
