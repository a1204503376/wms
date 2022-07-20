package org.nodes.wms.dao.outstock.so.dto.output;

import lombok.Data;

/**
 * pc拣货返回前端出库明细下拉组件dto
 */
@Data
public class LineNoAndSkuSelectResponse {
	/**
	 * 订单行号
	 */
	private String lineNo;
	/**
	 * 物料编码
	 */
	private String skuCode;
}
