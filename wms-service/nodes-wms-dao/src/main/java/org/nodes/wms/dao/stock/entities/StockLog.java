package org.nodes.wms.dao.stock.entities;

import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLotEntity;

import java.math.BigDecimal;

/**
 * 库存日志实体类
 **/
@Data
public class StockLog extends BaseSkuLotEntity {

	private static final long serialVersionUID = 1057813814281261519L;

	/**
	 * 库存日志id
	 */
	private Long wlslId;

	/**
	 * 库存日志类型
	 */
	private String logType;

	/**
	 * 来源的单据id
	 */
	private Long sourceBillId;

	/**
	 * 来源的单据编码
	 */
	private String sourceBillNo;

	/**
	 * 本次操作的待上架数量
	 */
	private BigDecimal currentStayStockQty;

	/**
	 * 本次操作的上架数量
	 */
	private BigDecimal currentStockQty;

	/**
	 * 本次操作的下架数量
	 */
	private BigDecimal currentPickQty;

	/**
	 * 库存id
	 */
	private Long stockId;

	/**
	 * 库存状态（0正常，1冻结）
	 */
	private Integer stockStatus;

	/**
	 * 层级
	 */
	private Integer skuLevel;

	/**
	 * 包装名称
	 */
	private String wspName;

	/**
	 * 包装id
	 */
	private Long wspId;

	/**
	 * 物品id
	 */
	private Long skuId;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 待上架数量(操作之前)
	 */
	private BigDecimal stayStockQty;

	/**
	 * 上架数量(操作之前)
	 */
	private BigDecimal stockQty;

	/**
	 * 下架数量(操作之前)
	 */
	private BigDecimal pickQty;

	/**
	 * 箱号
	 */
	private String boxCode;

	/**
	 * 托盘号
	 */
	private String lpnCode;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 库位id
	 */
	private Long locId;

	/**
	 * 库区编码
	 */
	private String zoneCode;

	/**
	 * 库区id
	 */
	private Long zoneId;

	/**
	 * 库房名称
	 */
	private String whName;

	/**
	 * 库房id
	 */
	private String whId;

	/**
	 * 货主id
	 */
	private String woId;
}
