package org.nodes.wms.dao.instock.receive.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.basics.sku.dto.output.SkuSelectResponse;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class EditReceiveDetailResponse implements Serializable {
	private static final long serialVersionUID = -1896492170322284739L;
	/**
	 * 收货单明细id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long receiveDetailId;
	/**
	 * sku下拉框对象
	 */
	private SkuSelectResponse sku;
	/**
	 * 计划数量
	 */
	private BigDecimal planQty;
	/**
	 * 实收数量
	 */
	private BigDecimal scanQty;

	/**
	 * 计量单位编码
	 */
	private String umCode;
	/**
	 * 规格
	 */
	private String skuSpec;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 行号
	 */
   private  String lineNumber;
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
	private String  skuLot6;
	/**
	 * CRCC
	 */
	private String skuLot8;
}
