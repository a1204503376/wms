package org.nodes.wms.dao.instock.receive.dto.output;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 按箱收货sku返回前端dto
 */
@Data
public class ReceiveDetailLpnItemDto {
	/**
	 * 物料编码
	 */
	private String SkuCode;
	/**
	 * 物料名称
	 */
	private String SkuName;
	/**
	 * 数量
	 */
	private BigDecimal planQty;
	/**
	 * 收货单明细id
	 */
	private Long receiveDetailId;
}
