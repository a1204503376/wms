package org.nodes.wms.dao.stock.dto.output;

import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存日志分页返回dto类
 **/
@Data
public class StockLogPageResponse extends BaseSkuLot implements Serializable {

	private static final long serialVersionUID = -1018653292802424467L;

	/**
	 * 库存日志类型
	 */
	private String logType;

	/**
	 * 来源的单据编码
	 */
	private String sourceBillNo;

	/**
	 * 本次操作待上架数量
	 */
	private BigDecimal currentStayStockQty;

	/**
	 * 本次操作上架数量
	 */
	private BigDecimal currentStockQty;

	/**
	 * 本次操作待下架数量
	 */
	private BigDecimal currentPickQty;

	/**
	 * 库存状态
	 */
	private StockStatusEnum stockStatus;

	/**
	 * 层级
	 */
	private String skuLevel;

	/**
	 * 包装名称
	 */
	private String wspName;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;

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
	 * 库区编码
	 */
	private String zoneCode;

	/**
	 * 库房名称
	 */
	private String whName;

	/**
	 * 货主
	 */
	private String ownerName;

	/**
	 * 创建人
	 */
	private String createUser;

	/**
	 * 创建时间
	 */
	private Date createTime;
}
