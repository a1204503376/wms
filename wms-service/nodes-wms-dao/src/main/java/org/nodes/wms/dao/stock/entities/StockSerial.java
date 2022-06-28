package org.nodes.wms.dao.stock.entities;

import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 序列号实体类
 **/
@Data
public class StockSerial extends TenantEntity {

	private static final long serialVersionUID = -453322099134024067L;

	/**
	 * 序列号id
	 */
	private Long serialId;

	/**
	 * 库存id
	 */
	private Long stockId;

	/**
	 * 库房id
	 */
	private Long whId;

	/**
	 * 序列号
	 */
	private String serialNumber;

	/**
	 * 序列号状态(0在库)
	 */
	private Integer serialState;

	/**
	 * 入库次数
	 */
	private Integer instockNumber;

	/**
	 * 物品id
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
}
