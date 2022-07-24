package org.nodes.wms.dao.outstock.so.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;

/**
 * pc拣货返回前端明细dto
 */
@Data
public class PickByPcSoDetailResponse {
	/**
	 * 发货单明细id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soDetailId;
	/**
	 * 行号
	 */
	private String soLineNo;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 物品id
	 */
	private Long skuId;
	/**
	 * 计划数量
	 */
	private BigDecimal planQty;
	/**
	 * 实收数量
	 */
	private BigDecimal scanQty;
	/**
	 * 剩余数量
	 */
	private BigDecimal surplusQty;
	/**
	 * 计量单位
	 */
	private String umCode;
	/**
	 * 批次
	 */
	private String skuLot1;
	/**
	 * 专属客户
	 */
	private String skuLot4;
}
