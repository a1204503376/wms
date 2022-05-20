package org.nodes.wms.dao.instock.receive.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.basics.sku.dto.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.entities.Sku;

import java.math.BigDecimal;

@Data
public class ReceiveDetailEditResponse {

	/**
	 * sku下拉框对象
	 */
	private SkuSelectResponse sku;
	/**
	 * 计划数量
	 */
	private BigDecimal planQty;

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
}
