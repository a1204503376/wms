package org.nodes.wms.dao.count.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 盘点单明细请求对象
 */
@Data
public class FindPdaStockCountDetailResponseListRequest implements Serializable {

	private static final long serialVersionUID = -6632615045154856448L;
	/**
	 * 盘点单ID
	 */
	private Long countBillId;
}
