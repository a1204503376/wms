package org.nodes.wms.dao.outstock.so.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.stock.dto.output.PickByPcStockResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * pc拣货返回前端dto
 */
@Data
public class SoDetailAndStockResponse {
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
	 * 批次号
	 */
	private String lotNumber;
	/**
	 * 可出库库存集合
	 */
	List<PickByPcStockResponse> stockResponseList;

}
