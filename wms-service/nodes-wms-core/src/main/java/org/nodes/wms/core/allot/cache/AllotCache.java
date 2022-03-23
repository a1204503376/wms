package org.nodes.wms.core.allot.cache;

import org.nodes.core.tool.cache.SerialNoCache;

/**
 * author: pengwei
 * date: 2021/4/20 13:32
 * description: AllotCache
 */
public class AllotCache {
	/**
	 * 获取调拨单单号
	 *
	 * @return 调拨单单号
	 */
	public static String getAllotBillNo() {
		return "AT" + SerialNoCache.getSerialDateNo("Allot", 8);
	}
}
