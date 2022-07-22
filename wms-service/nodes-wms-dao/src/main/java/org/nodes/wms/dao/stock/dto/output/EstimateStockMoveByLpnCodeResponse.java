package org.nodes.wms.dao.stock.dto.output;

import lombok.Data;

import java.io.Serializable;

/**
 * 根据Lpn判断库存是否可移动响应对象
 */
@Data
public class EstimateStockMoveByLpnCodeResponse implements Serializable {
	private static final long serialVersionUID = 3330654122944090823L;
}
