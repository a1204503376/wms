package org.nodes.wms.core.instock.asn.cache;

import org.nodes.core.tool.cache.SerialNoCache;
import org.nodes.wms.core.instock.asn.dto.AsnHeaderDTO;
import org.springblade.core.cache.utils.CacheUtil;

import static org.nodes.core.base.cache.CacheNames.NODES_FLASH;

/**
 * author: pengwei
 * date: 2021/4/12 14:49
 * description: 入库缓存类
 */
public class AsnCache {
	private static final String ASNHEADER_CACHE_BILL_NO = "asnheader:billno";
	/**
	 * 获取入库单编码
	 *
	 * @return 新的入库单编码
	 */
	public static String getAsnBillNo() {
		return "AN" + SerialNoCache.getSerialDateNo("ASN", 8);
	}

	/**
	 * 获取采购订单编码
	 *
	 * @return 新的采购订单编码
	 */
	public static String getPoBillNo() {
		return SerialNoCache.getSerialDateNo("PO", 8);
	}

	/**
	 * 获取收货清单编码
	 *
	 * @return
	 */
	public static String getInventoryNo() {
		return SerialNoCache.getSerialDateNo("ASN-INVENTORY", 8);
	}
	/**
	 * 存储入库单缓存
	 *
	 * @param billNo       入库单编码
	 * @param asnHeaderDTO 入库单信息
	 */
	public static void put(String billNo, AsnHeaderDTO asnHeaderDTO) {
		CacheUtil.put(NODES_FLASH, ASNHEADER_CACHE_BILL_NO, billNo, asnHeaderDTO);
	}
	/**
	 * 获得入库单信息
	 *
	 * @param billNo 入库单编码
	 * @return 入库单信息
	 */
	public static AsnHeaderDTO getDTOByBillNo(String billNo) {
		return (AsnHeaderDTO) CacheUtil.get(NODES_FLASH, ASNHEADER_CACHE_BILL_NO, billNo);
	}

	/**
	 * 从缓存中移除
	 *
	 * @param billNo 入库单编码
	 */
	public static void remove(String billNo) {
		CacheUtil.evict(NODES_FLASH, ASNHEADER_CACHE_BILL_NO, billNo);
	}

}
