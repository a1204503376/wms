package org.nodes.wms.dao.count.dto.input;

import lombok.Data;

/**
 * 盘点单明细请求对象
 */
@Data
public class FindPdaStockCountDetailResponseListRequest {

	/**
	 * 盘点单ID
	 */
	private Long countBillId;
}
