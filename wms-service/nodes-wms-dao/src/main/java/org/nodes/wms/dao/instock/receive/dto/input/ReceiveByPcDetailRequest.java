package org.nodes.wms.dao.instock.receive.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * pc收货接收前端明细dto
 */
@Data
public class ReceiveByPcDetailRequest {
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull(message = "收货单明细id不能为空")
	private Long receiveDetailId;
	/**
	 * 订单行号
	 */
	private String lineNumber;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 实收数量
	 */
	@NotNull(message = "实收数量不能为空")
	private BigDecimal scanQty;
	/**
	 * 计量单位编码
	 */
	private String umCode;
	/**
	 * 库位编码
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull(message = "库位不能为空")
	private Long locId;
	/**
	 * 箱码
	 */
	private String boxCode;
	/**
	 * lpn
	 */
	private String lpnCode;
	/**
	 * 序列号
	 */
	private String snCode;

	/**
	 * 生产批次
	 */
	private String skuLot1;
	/**
	 * 规格
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
	 * CRCC
	 */
	private String skuLot8;
}
