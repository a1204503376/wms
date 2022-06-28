package org.nodes.wms.dao.instock.receive.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
	private String skuCode;
	/**
	 * 物料名称
	 */
	private String skuName;
	/**
	 * 物料id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

	/**
	 * 数量
	 */
	private BigDecimal planQty;
	/**
	 * 收货单明细id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long receiveDetailId;
	/**
	 * lpn表id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long receiveDetailLpnId;
}
