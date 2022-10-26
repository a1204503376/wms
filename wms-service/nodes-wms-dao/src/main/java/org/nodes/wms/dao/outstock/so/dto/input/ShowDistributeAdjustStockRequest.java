package org.nodes.wms.dao.outstock.so.dto.input;

import lombok.Data;

/**
 * 分配调整页面，根据箱码或库位编码查看库存请求类
 **/
@Data
public class ShowDistributeAdjustStockRequest {

	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * 库房id
	 */
	private Long whId;

	/**
	 * 库位编码
	 */
	private String locCode;
}
