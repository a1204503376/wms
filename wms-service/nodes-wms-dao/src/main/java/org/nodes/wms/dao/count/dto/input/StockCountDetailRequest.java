package org.nodes.wms.dao.count.dto.input;

import lombok.Data;

/**
 * 盘点单明细请求对象
 */
@Data
public class StockCountDetailRequest {

	/**
	 * 盘点单明细ID
	 */
	private Long countDetailId;
	/**
	 * 盘点单ID
	 */
	private Long countBillId;
	/**
	 * 盘点单编码
	 */
	private String countBillNo;
	/**
	 * 库位ID
	 */
	private Long locId;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 箱号
	 */
	private String boxCode;
	/**
	 * 盘点人
	 */
	private String userName;
}
