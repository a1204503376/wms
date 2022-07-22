package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 根据箱码集合判断库存是否可移动请求对象
 */
@Data
public class EstimateStockMoveByBoxCodeRequest implements Serializable {
	private static final long serialVersionUID = 3118006839792085277L;
	/**
	 * 箱码
	 */
	private List<String> boxCodeList;
}
