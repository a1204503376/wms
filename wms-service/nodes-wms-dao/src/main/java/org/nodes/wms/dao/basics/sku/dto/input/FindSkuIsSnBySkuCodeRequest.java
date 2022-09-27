package org.nodes.wms.dao.basics.sku.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

@Data
public class FindSkuIsSnBySkuCodeRequest implements Serializable {
	private static final long serialVersionUID = 3841242546508057617L;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 批次号
	 */
	private String skuLot1;
	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
}
