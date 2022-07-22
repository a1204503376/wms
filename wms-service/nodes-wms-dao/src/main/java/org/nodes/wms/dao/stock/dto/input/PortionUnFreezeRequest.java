package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 库存冻结请求对象
 */
@Data
public class PortionUnFreezeRequest implements Serializable {
	private static final long serialVersionUID = -7524460869718941418L;
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
	private String lotNumber;
	/**
     * 序列号
	 */
	private String serialNumber;
}
