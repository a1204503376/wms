package org.nodes.wms.dao.instock.receive.dto.output;

import lombok.Data;

import java.math.BigDecimal;

/**
 * pc收货明细返回前端dto
 */
@Data
public class ReceiveDetailByPcResponse {
	/**
	 * 订单行号
	 */
	private String lineNo;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 剩余数量
	 */
	private BigDecimal surplusQty;
	/**
	 * 计量单位编码
	 */
	private String umCode;
	/**
	 * 生产批次
	 */
	private String skuLot1;
	/**
	 * 客户
	 */
	private String skuLot4;
	/**
	 * 钢背批次
	 */
	private String skuLot5;
	/**
	 * 摩擦块批次
	 */
	private String skuLot6;
	/**
	 * CRCC
	 */
	private String skuLot8;
}
