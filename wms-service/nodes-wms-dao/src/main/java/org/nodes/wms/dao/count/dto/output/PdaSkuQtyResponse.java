package org.nodes.wms.dao.count.dto.output;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * PDA显示物品及其数量返回对象
 */
@Data
public class PdaSkuQtyResponse implements Serializable {

	private static final long serialVersionUID = -1058124541100303293L;
	/**
	 * 物品ID
	 */
	@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 库存余额(上架量 - 下架量)
	 * 包括：冻结量和占用量
	 */
	@JsonSerialize
	private BigDecimal stockBalance;

	/**
	 * 箱号
	 */
	private String boxCode;
	/**
	 * 库位编码
	 */
	private String locCode;
}
