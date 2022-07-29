package org.nodes.wms.dao.stock.dto.output;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 判断库存是否可移动响应对象
 */
@Data
public class FindAllSerialNumberManageResponse implements Serializable {
	private static final long serialVersionUID = 1855271549168247733L;
	/**
	 * 是否序列号管理 true为是 false为否
	 */
	private Boolean isSn;
	/**
	 * 序列号集合
	 */
	private List<String> serialNumberList;
	/**
	 * 拆箱库存响应对象
	 */
	private List<DevanningStockResponse> stockList;
}
