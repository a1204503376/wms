package org.nodes.wms.core.outstock.so.cache;

import org.nodes.core.tool.cache.SerialNoCache;

/**
 * author: pengwei
 * date: 2021/4/19 16:14
 * description: SoCache
 */
public class SoCache {
	/**
	 * 获取出库单单号
	 *
	 * @return 出库单单号
	 */
	public static String getSoBillNo() {
		return "SO" + SerialNoCache.getSerialDateNo("SO", 8);
	}

	/**
	 * 获取销售单单号
	 * @return
	 */
	public static String getSalesBillNo(){
		return SerialNoCache.getSerialDateNo("Sales", 8);
	}

	/**
	 * 获取发货清单单号
	 * @return
	 */
	public static String getInventoryNo(){
		return SerialNoCache.getSerialDateNo("SO-INVENTORY", 8);
	}
}
