package org.nodes.wms.dao.instock.receive.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;

/**
 * pc收货明细返回前端dto
 */
@Data
public class ReceiveDetailByPcResponse {
	/**
	 * 收货单明细id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long receiveDetailId;
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
	 * 规格型号
	 */
	private String skuSpec;
	/**
	 * 生产批次
	 */
	private String skuLot1;
	/**
	 * 规格型号
	 */
	private String skuLot2;
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
	 * 适用速度等级
	 */
	private String skuLot8;
	/**
	 * 生产日期
	 */
	private String skuLot9;
	/**
	 * 序列号
	 */
	private String snNumber;
}
