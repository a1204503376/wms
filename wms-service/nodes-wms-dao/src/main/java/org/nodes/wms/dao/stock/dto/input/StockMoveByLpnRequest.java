package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 库存移动请求对象
 */
@Data
public class StockMoveByLpnRequest implements Serializable {
	private static final long serialVersionUID = 6251527073571241801L;
	/**
	 * 目标库位编码
	 */
	private String targetLocCode;
	/**
	 * lpn编码-托盘号
	 */
	private String lpnCode;
}
