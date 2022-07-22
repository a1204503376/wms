package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 根据Lpn判断库存是否可移动请求对象
 */
@Data
public class EstimateStockMoveByLpnCodeRequest implements Serializable {
	private static final long serialVersionUID = 8371969072186711839L;
	/**
	 * Lpn编码---托盘号
	 */
	private String lpnCode;
}
