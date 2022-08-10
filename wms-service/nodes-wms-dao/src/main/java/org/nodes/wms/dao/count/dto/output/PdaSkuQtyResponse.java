package org.nodes.wms.dao.count.dto.output;

import lombok.Data;

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
	private BigDecimal stockQty;
}
