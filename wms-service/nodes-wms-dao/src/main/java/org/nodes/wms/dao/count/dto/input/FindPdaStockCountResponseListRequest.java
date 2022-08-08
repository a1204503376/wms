package org.nodes.wms.dao.count.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 盘点单编辑请求对象
 */
@Data
public class FindPdaStockCountResponseListRequest {
	/**
	 * 盘点单编码
	 */
	private String countBillNo;
	/**
	 * 库房ID
	 */
	@NotNull
	private Long whId;
}
