package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * PC库存移动请求对象
 */
@Data
public class StockPcMoveRequest implements Serializable {

	private static final long serialVersionUID = 6434626213064038716L;

	/**
	 * 库存id
	 */
	private Long stockId;

	/**
	 * 移动数据
	 */
	private List<StockPcMoveDetailRequest> stockMoveDataList;
}
