package org.nodes.wms.biz.stock;

import java.util.List;

/**
 * 序列号管理BIZ
 */
public interface SerialBiz {
	/**
	 * 根据stockid获取在库的序列号列表
	 *
	 * @param stockId 库存id
	 * @return 序列号集合
	 */
	List<String> findSerialNoByStockId(Long stockId);
}
